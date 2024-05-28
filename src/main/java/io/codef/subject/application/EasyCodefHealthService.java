package io.codef.subject.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.subject.application.dto.request.HealthCheckRequest;
import io.codef.subject.application.dto.response.MultipleAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EasyCodefHealthService {
    private final ObjectMapper objectMapper;
    private final EasyCodef codef;

    @SneakyThrows
    public MultipleAuthResponse requestSimpleAuthToNhis(HealthCheckRequest request) {
        String uuid = UUID.randomUUID().toString();
        String currentYear = String.valueOf(LocalDateTime.now().getYear());

        HashMap<String, Object> parameterMap = new HashMap<>();

        putSimpleAuthInfo(parameterMap);
        putSimpleAuthUserInfo(request, parameterMap);
        parameterMap.put("id", uuid);
        parameterMap.put("searchStartYear", currentYear);
        parameterMap.put("searchEndYear", currentYear);

        String result2024 = codef.requestProduct(TargetUrl.NHIS_HEALTH_CHECKUP_SHORT_URL.getUrl(), EasyCodefServiceType.DEMO, parameterMap);

        JsonNode jsonNode = objectMapper.readTree(result2024);

        String transactionId = jsonNode.get("data").get("jti").asText();
        String twoWayTimestamp = jsonNode.get("data").get("twoWayTimestamp").asText();
        String jobIndex = jsonNode.get("data").get("jobIndex").asText();
        String threadIndex = jsonNode.get("data").get("threadIndex").asText();

        return new MultipleAuthResponse(uuid, jobIndex, threadIndex, transactionId, twoWayTimestamp);
    }

    @SneakyThrows
    public String requestCertification(
            MultipleAuthResponse authResponse,
            HealthCheckRequest request
    ) {
        HashMap<String, Object> parameterMap = new HashMap<>();
        HashMap<String, Object> twoWayInfo = new HashMap<>();
        HashMap<String, Object> extraInfo = new HashMap<>();

        putSimpleAuthInfo(parameterMap);
        putSimpleAuthUserInfo(request, parameterMap);
        parameterMap.put("id", authResponse.id());
        parameterMap.put("searchStartYear", "2024");
        parameterMap.put("searchEndYear", "2024");

        parameterMap.put("simpleAuth", "1");
        parameterMap.put("is2Way", true);

        twoWayInfo.put("jobIndex", Long.parseLong(authResponse.jobIndex()));
        twoWayInfo.put("threadIndex", Long.parseLong(authResponse.threadIndex()));
        twoWayInfo.put("jti", authResponse.transactionId());
        twoWayInfo.put("twoWayTimestamp", Long.parseLong(authResponse.timestamp()));
        twoWayInfo.put("continue2Way", true);
        twoWayInfo.put("method", "simpleAuth");
        extraInfo.put("commSimpleAuth", "");
        twoWayInfo.put("extraInfo", extraInfo);
        parameterMap.put("twoWayInfo", twoWayInfo);

        return codef.requestCertification(TargetUrl.NHIS_HEALTH_CHECKUP_SHORT_URL.getUrl(), EasyCodefServiceType.DEMO, parameterMap);
    }

    @SneakyThrows
    public String requestHealthCheckResponse(
            MultipleAuthResponse authResponse,
            HealthCheckRequest request,
            String targetYear
    ) {
        HashMap<String, Object> parameterMap = new HashMap<>();

        putSimpleAuthInfo(parameterMap);
        putSimpleAuthUserInfo(request, parameterMap);
        parameterMap.put("id", authResponse.id());
        parameterMap.put("searchStartYear", targetYear);
        parameterMap.put("searchEndYear", targetYear);

        return codef.requestProduct(TargetUrl.NHIS_HEALTH_CHECKUP_SHORT_URL.getUrl(), EasyCodefServiceType.DEMO, parameterMap);
    }

    private void putSimpleAuthInfo(HashMap<String, Object> parameterMap) {
        parameterMap.put("organization", "0002");
        parameterMap.put("loginType", "5");
        parameterMap.put("loginTypeLevel", "1");
        parameterMap.put("inquiryType", "4");
        parameterMap.put("type", "1");
    }

    private void putSimpleAuthUserInfo(HealthCheckRequest request, HashMap<String, Object> parameterMap) {
        parameterMap.put("userName", request.userName());
        parameterMap.put("phoneNo", request.phoneNo());
        parameterMap.put("identity", request.identity());
        parameterMap.put("telecom", request.telecom());
    }
}
