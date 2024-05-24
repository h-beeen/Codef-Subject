package io.codef.subject.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.subject.application.dto.request.HealthCheckRequest;
import io.codef.subject.application.dto.response.MultipleResponse;
import io.codef.subject.infra.persistence.ConnectedIdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EasyCodefHealthService {
    private static final String productUrl = "/v1/kr/public/pp/nhis-health-checkup/result";

    private final ObjectMapper objectMapper;
    private final EasyCodef codef;
    private final ConnectedIdRepository connectedIdRepository;
    private final PropertyResolver propertyResolver;

    private CompletableFuture<String> createAsyncRequest(HashMap<String, Object> parameterMap, int delay) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 딜레이 적용
                TimeUnit.MILLISECONDS.sleep(delay);
                // 비동기 작업 실행
                String s = codef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);
                log.warn("request Product = {}", s);
            } catch (InterruptedException | UnsupportedEncodingException | JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public MultipleResponse requestHealthSimpleAuth(HealthCheckRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("id", UUID.randomUUID().toString());
        parameterMap.put("organization", "0002");
        parameterMap.put("loginType", "5");
        parameterMap.put("loginTypeLevel", "1");
        parameterMap.put("userName", request.userName());
        parameterMap.put("phoneNo", request.phoneNo());
        parameterMap.put("identity", request.identity());
        parameterMap.put("inquiryType", "4");
        parameterMap.put("searchStartYear", "2024");
        parameterMap.put("searchEndYear", "2024");
        parameterMap.put("type", "1");
        parameterMap.put("telecom", request.telecom());

        String result2024 = codef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);

        // JSON 문자열을 JsonNode로 파싱
        JsonNode jsonNode = objectMapper.readTree(result2024);

        String transactionId = jsonNode.get("data").get("jti").asText();
        String twoWayTimestamp = jsonNode.get("data").get("twoWayTimestamp").asText();

        return new MultipleResponse(transactionId, twoWayTimestamp);
    }
}
