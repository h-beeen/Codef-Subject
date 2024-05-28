package io.codef.subject.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.api.EasyCodefUtil;
import io.codef.subject.application.dto.request.ConnectedIdRequest;
import io.codef.subject.application.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EasyCodef codef;

    /**
     * 커넥티드 아이디 신규 생성
     */
    public String initConnectedId(ConnectedIdRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        List<HashMap<String, Object>> accountList = initAccountList(request);
        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("accountList", accountList);
        return codef.createAccount(EasyCodefServiceType.DEMO, parameterMap);
    }

    /**
     * 커넥티드 아이디 계정 추가
     */
    public String addAccountOnConnectedId(ConnectedIdRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        List<HashMap<String, Object>> accountList = initAccountList(request);
        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("accountList", accountList);
        parameterMap.put("connectedId", request.connectedId());
        return codef.addAccount(EasyCodefServiceType.DEMO, parameterMap);
    }

    /**
     * 보유 커넥티드 아이디 전체 조회
     */
    public String getConnectedIds() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return codef.getConnectedIdList(EasyCodefServiceType.DEMO);
    }

    /**
     * 토큰 발급
     */
    public TokenResponse getToken() throws IOException {
        String result = codef.requestToken(EasyCodefServiceType.DEMO);
        return new TokenResponse(result);
    }

    
    private List<HashMap<String, Object>> initAccountList(ConnectedIdRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        HashMap<String, Object> accountMap = new HashMap<>();

        accountMap.put("id", request.id());
        accountMap.put("organization", request.organizationCode());
        accountMap.put("countryCode", "KR");
        accountMap.put("businessType", "BK");
        accountMap.put("clientType", "P");
        accountMap.put("loginType", "1");
        accountMap.put("password", EasyCodefUtil.encryptRSA(request.password(), codef.getPublicKey()));

        return List.of(accountMap);
    }
}