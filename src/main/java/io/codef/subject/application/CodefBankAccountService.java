package io.codef.subject.application;

import io.codef.subject.application.dto.request.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.codef.subject.application.TargetUrl.BANK_ACCOUNT_FULL_URL;
import static io.codef.subject.application.TargetUrl.BANK_TRANSACTION_FULL_URL;

@Service
@RequiredArgsConstructor
public class CodefBankAccountService {
    private final RestTemplate restTemplate;

    @Value("${codef.connected-id}")
    private String connectedId;

    @Value("${codef.bearer-token}")
    private String token;

    /**
     * 은행 계좌 조회 (기관코드 기준)
     * Bearer Token 기준
     */
    public String getBankAccountResponses(String organizationCode) {
        HashMap<String, Object> parameterMap = initBankAccountParameterMap(organizationCode);
        HttpHeaders headers = initAuthorizationHeader();
        return getResponse(parameterMap, headers, BANK_ACCOUNT_FULL_URL);
    }

    /**
     * 수시입출거래내역 조회 (기관코드 및 계좌번호 기준)
     * Bearer Token 기준
     */
    public String getBankTransactionResponses(TransactionRequest request) {
        HashMap<String, Object> parameterMap = initBankTransactionParameterMap(request);
        HttpHeaders headers = initAuthorizationHeader();
        return getResponse(parameterMap, headers, BANK_TRANSACTION_FULL_URL);
    }


    private HashMap<String, Object> initBankAccountParameterMap(String organizationCode) {
        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("organization", organizationCode);
        parameterMap.put("connectedId", connectedId);
        return parameterMap;
    }

    private HashMap<String, Object> initBankTransactionParameterMap(TransactionRequest request) {
        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("connectedId", connectedId);
        parameterMap.put("organization", request.organizationCode());
        parameterMap.put("account", request.withdrawAccountNo());
        parameterMap.put("startDate", "20230101");
        parameterMap.put("endDate", "20240101");
        parameterMap.put("orderBy", "0");
        return parameterMap;
    }

    private String getResponse(
            HashMap<String, Object> parameterMap,
            HttpHeaders headers,
            TargetUrl bankTransactionUrl
    ) {
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(parameterMap, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(bankTransactionUrl.getUrl(), HttpMethod.POST, entity, String.class);
        return URLDecoder.decode(Objects.requireNonNull(exchange.getBody()), StandardCharsets.UTF_8);
    }

    private HttpHeaders initAuthorizationHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
