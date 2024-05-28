package io.codef.subject.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.subject.application.dto.request.HealthCheckRequest;
import io.codef.subject.application.dto.response.MultipleAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class EasyCodefHealthService {
    private final ObjectMapper objectMapper;
    private final EasyCodef codef;


    @SneakyThrows
    public MultipleAuthResponse requestSimpleAuthToNhis(HealthCheckRequest request) {
        String uuid = UUID.randomUUID().toString();
        HashMap<String, Object> parameterMap = new HashMap<>();

        putSimpleAuthInfo(parameterMap);
        putSimpleAuthUserInfo(request, parameterMap);
        parameterMap.put("id", uuid);
        String result2024 = codef.requestProduct(TargetUrl.NHIS_HEALTH_CHECKUP_SHORT_URL.getUrl(), EasyCodefServiceType.DEMO, parameterMap);

        JsonNode jsonNode = objectMapper.readTree(result2024);
        HashMap<String, Object> dataObject = objectMapper.convertValue(jsonNode.get("data"), new TypeReference<>() {
        });

        return new MultipleAuthResponse(uuid, dataObject);
    }

    @SneakyThrows
    public String requestCertification(MultipleAuthResponse authResponse, HealthCheckRequest request) {
        HashMap<String, Object> parameterMap = new HashMap<>();

        putSimpleAuthInfo(parameterMap);
        putSimpleAuthUserInfo(request, parameterMap);

        parameterMap.put("id", authResponse.id());
        parameterMap.put("simpleAuth", "1");
        parameterMap.put("is2Way", true);
        parameterMap.put("twoWayInfo", authResponse.data());

        return codef.requestCertification(TargetUrl.NHIS_HEALTH_CHECKUP_SHORT_URL.getUrl(), EasyCodefServiceType.DEMO, parameterMap);
    }

    public List<CompletableFuture<String>> requestHealthCheckResponses(MultipleAuthResponse authResponse, HealthCheckRequest request) {
        int currentYear = LocalDateTime.now().getYear();
        List<String> targetYears = IntStream.range(1, 5)
                .mapToObj(i -> String.valueOf(currentYear - i))
                .toList();

        return targetYears.stream().map(year -> CompletableFuture.supplyAsync(() -> requestHealthCheckResponse(authResponse, request, year))).toList();
    }

    @SneakyThrows
    private String requestHealthCheckResponse(
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

    private void putSimpleAuthUserInfo(
            HealthCheckRequest request,
            HashMap<String, Object> parameterMap
    ) {
        String currentYear = String.valueOf(LocalDateTime.now().getYear());
        parameterMap.put("searchStartYear", currentYear);
        parameterMap.put("searchEndYear", currentYear);
        parameterMap.put("userName", request.userName());
        parameterMap.put("phoneNo", request.phoneNo());
        parameterMap.put("identity", request.identity());
        parameterMap.put("telecom", request.telecom());
    }
}
