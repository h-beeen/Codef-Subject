package io.codef.subject.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.api.EasyCodefUtil;
import io.codef.subject.application.dto.request.AddAccountRequest;
import io.codef.subject.application.dto.request.ConnectedIdRequest;
import io.codef.subject.application.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EasyCodef codef;
    private final ObjectMapper objectMapper;

    public Map initConnectedId(ConnectedIdRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        List<HashMap<String, Object>> accountList = new ArrayList<>();
        HashMap<String, Object> accountMap = new HashMap<>();

        accountMap.put("id", request.id());
        accountMap.put("organization", request.organizationCode());
        accountMap.put("countryCode", "KR");
        accountMap.put("businessType", "BK");
        accountMap.put("clientType", "P");
        accountMap.put("loginType", "1");

        try {
            accountMap.put("password", EasyCodefUtil.encryptRSA(request.password(), codef.getPublicKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        accountList.add(accountMap);

        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("accountList", accountList);

        String result = codef.createAccount(EasyCodefServiceType.DEMO, parameterMap);
        return objectMapper.readValue(result, Map.class);
    }

    public String addAccountOnConnectedId(AddAccountRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        List<HashMap<String, Object>> accountList = new ArrayList<>();
        HashMap<String, Object> accountMap = new HashMap<>();

        accountMap.put("id", request.id());
        accountMap.put("organization", request.organizationCode());
        accountMap.put("countryCode", "KR");
        accountMap.put("businessType", "BK");
        accountMap.put("clientType", "P");
        accountMap.put("loginType", "1");

        try {
            accountMap.put("password", EasyCodefUtil.encryptRSA(request.password(), codef.getPublicKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        accountList.add(accountMap);

        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("accountList", accountList);
        parameterMap.put("connectedId", request.connectedId());
        return codef.addAccount(EasyCodefServiceType.DEMO, parameterMap);
    }

    public String getConnectedIds() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return codef.getConnectedIdList(EasyCodefServiceType.DEMO);
    }

    public TokenResponse getToken() throws IOException {
        String result = codef.requestToken(EasyCodefServiceType.DEMO);
        return new TokenResponse(result);
    }
}
