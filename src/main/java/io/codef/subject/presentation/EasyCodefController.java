package io.codef.subject.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.subject.application.AuthenticationService;
import io.codef.subject.application.EasyCodefBankAccountService;
import io.codef.subject.application.EasyCodefHealthService;
import io.codef.subject.application.dto.request.HealthCheckRequest;
import io.codef.subject.application.dto.request.TransactionRequest;
import io.codef.subject.application.dto.response.MultipleResponse;
import io.codef.subject.application.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/easy")
public class EasyCodefController {

    private final EasyCodefBankAccountService easyCodefBankAccountService;
    private final AuthenticationService authenticationService;
    private final EasyCodefHealthService easyCodefHealthService;


    /**
     * 보유계좌 조회 (EasyCodef Ver)
     * 국민은행 (0004) / 우리은행 (0020) 커넥티드 아이디 등록 완료
     * request : [GET] localhost:8080/api/v1/easy/bank?organizationCode=0020
     */
    @PostMapping("/bank/accountList")
    public ResponseEntity<String> getBankAccountResponse(@RequestParam String organizationCode) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return ResponseEntity.ok(easyCodefBankAccountService.getBankAccountResponse(organizationCode));
    }

    @PostMapping("/bank/transaction")
    public ResponseEntity<String> getBankTransactionResponse(@RequestBody TransactionRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return ResponseEntity.ok(easyCodefBankAccountService.getAccountTransactionResponse(request));
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getTokenResponse() throws IOException {
        TokenResponse token = authenticationService.getToken();
        return ResponseEntity.ok(token);
    }

    /**
     * 최근 1년 단건 조회
     */
    @PostMapping("/health-check")
    public ResponseEntity<MultipleResponse> getHealthCheckResponse(@RequestBody HealthCheckRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        MultipleResponse healthResponse = easyCodefHealthService.requestHealthSimpleAuth(request);


        return ResponseEntity.ok(healthResponse);
    }

}
