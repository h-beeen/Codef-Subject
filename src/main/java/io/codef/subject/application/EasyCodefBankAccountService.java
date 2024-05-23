package io.codef.subject.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EasyCodefBankAccountService {

    private final EasyCodef codef;

    public String getKbBankAccountResponse() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("connectedId", "65qNE0LWAOs8KSuRreGX0l");
        parameterMap.put("organization", "0004");

        String productUrl = "/v1/kr/bank/p/account/account-list";
        return codef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);
    }
}
