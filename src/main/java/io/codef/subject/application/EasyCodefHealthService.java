package io.codef.subject.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.subject.application.dto.request.HealthCheckRequest;
import io.codef.subject.application.dto.response.MultipleAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EasyCodefHealthService {
    private static final String productUrl = "/v1/kr/public/pp/nhis-health-checkup/result";

    private final ObjectMapper objectMapper;
    private final EasyCodef codef;

    private static void putSimpleAuthInfo(HashMap<String, Object> parameterMap) {
        parameterMap.put("organization", "0002");
        parameterMap.put("loginType", "5");
        parameterMap.put("loginTypeLevel", "1");
        parameterMap.put("inquiryType", "4");
        parameterMap.put("type", "1");
    }

    private static void putSimpleAuthUserInfo(HealthCheckRequest request, HashMap<String, Object> parameterMap) {
        parameterMap.put("userName", request.userName());
        parameterMap.put("phoneNo", request.phoneNo());
        parameterMap.put("identity", request.identity());
        parameterMap.put("telecom", request.telecom());
    }

    public MultipleAuthResponse requestHealthSimpleAuth(HealthCheckRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        HashMap<String, Object> parameterMap = new HashMap<>();
        String uuid = UUID.randomUUID().toString();

        putSimpleAuthInfo(parameterMap);
        putSimpleAuthUserInfo(request, parameterMap);
        parameterMap.put("id", uuid);
        parameterMap.put("searchStartYear", "2024");
        parameterMap.put("searchEndYear", "2024");

        String result2024 = codef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);

        log.warn("{}", result2024);
        JsonNode jsonNode = objectMapper.readTree(result2024);

        String transactionId = jsonNode.get("data").get("jti").asText();
        String twoWayTimestamp = jsonNode.get("data").get("twoWayTimestamp").asText();
        String jobIndex = jsonNode.get("data").get("jobIndex").asText();
        String threadIndex = jsonNode.get("data").get("threadIndex").asText();

        return new MultipleAuthResponse(uuid, jobIndex, threadIndex, transactionId, twoWayTimestamp);
    }

    public String requestHealthCheckResponse(MultipleAuthResponse authResponse, HealthCheckRequest request, String targetYear) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        HashMap<String, Object> parameterMap = new HashMap<>();

        putSimpleAuthInfo(parameterMap);
        putSimpleAuthUserInfo(request, parameterMap);
        parameterMap.put("id", authResponse.id());
        parameterMap.put("searchStartYear", targetYear);
        parameterMap.put("searchEndYear", targetYear);

        return codef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);
    }

    public String requestCertification(MultipleAuthResponse authResponse, HealthCheckRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
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

        String result = codef.requestCertification(productUrl, EasyCodefServiceType.DEMO, parameterMap);

        log.warn("request = {}", parameterMap);
        log.warn("{}", result);
        return result;
    }
}
