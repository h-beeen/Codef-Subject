package io.codef.subject.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.subject.application.dto.request.TransactionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EasyCodefBankAccountService {

    private final EasyCodef codef;

    @Value("${codef.connected-id}")
    private String connectedId;

    public String getBankAccountResponse(String organizationCode) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        HashMap<String, Object> parameterMap = new HashMap<>();

        parameterMap.put("connectedId", connectedId);
        parameterMap.put("organization", organizationCode);

        String productUrl = "/v1/kr/bank/p/account/account-list";
        return codef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);
    }

    public String getAccountTransactionResponse(TransactionRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        HashMap<String, Object> parameterMap = new HashMap<>();

        parameterMap.put("connectedId", connectedId);
        parameterMap.put("organization", request.organizationCode());
        parameterMap.put("account", request.withdrawAccountNo());
        parameterMap.put("startDate", "20230101");
        parameterMap.put("endDate", "20240101");
        parameterMap.put("orderBy", "0");

        String productUrl = "/v1/kr/bank/p/account/transaction-list";
        return codef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);
    }
}
