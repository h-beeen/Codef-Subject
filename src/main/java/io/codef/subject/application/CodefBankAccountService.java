package io.codef.subject.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.subject.application.dto.request.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CodefBankAccountService {
    private static final String BANK_ACCOUNT_URL = "https://development.codef.io/v1/kr/bank/p/account/account-list";
    private static final String BANK_TRANSACTION_URL = "https://development.codef.io/v1/kr/bank/p/account/transaction-list";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${codef.connected-id}")
    private String connectedId;

    @Value("${codef.bearer-token}")
    private String token;

    public ResponseEntity<Map<String, Object>> getBankAccountResponses(String organizationCode) throws JsonProcessingException, UnsupportedEncodingException {
        HashMap<String, Object> parameterMap = new HashMap<>();

        parameterMap.put("organization", organizationCode);
        parameterMap.put("connectedId", connectedId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(parameterMap, headers);

        // Send POST request
        ResponseEntity<String> exchange = restTemplate.exchange(BANK_ACCOUNT_URL, HttpMethod.POST, entity, String.class);

        String decodedResponse = URLDecoder.decode(exchange.getBody(), StandardCharsets.UTF_8);
        Map<String, Object> responseBody = objectMapper.readValue(decodedResponse, Map.class);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> getBankTransactionResponses(TransactionRequest request) throws JsonProcessingException, UnsupportedEncodingException {
        HashMap<String, Object> parameterMap = new HashMap<>();

        parameterMap.put("connectedId", connectedId);
        parameterMap.put("organization", request.organizationCode());
        parameterMap.put("account", request.withdrawAccountNo());
        parameterMap.put("startDate", "20230101");
        parameterMap.put("endDate", "20240101");
        parameterMap.put("orderBy", "0");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(parameterMap, headers);

        // Send POST request
        ResponseEntity<String> exchange = restTemplate.exchange(BANK_TRANSACTION_URL, HttpMethod.POST, entity, String.class);

        String decodedResponse = URLDecoder.decode(exchange.getBody(), StandardCharsets.UTF_8);
        Map<String, Object> responseBody = objectMapper.readValue(decodedResponse, Map.class);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
