package io.codef.subject.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.api.EasyCodefUtil;
import io.codef.subject.application.dto.ConnectedIdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectedIdService {

    private final EasyCodef codef;

    public void initConnectedId(ConnectedIdRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
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
        
        codef.createAccount(EasyCodefServiceType.DEMO, parameterMap);
    }
}
