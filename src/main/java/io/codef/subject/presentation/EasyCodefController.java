package io.codef.subject.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.subject.application.AuthenticationService;
import io.codef.subject.application.EasyCodefBankAccountService;
import io.codef.subject.application.EasyCodefHealthService;
import io.codef.subject.application.dto.request.HealthCheckRequest;
import io.codef.subject.application.dto.request.TransactionRequest;
import io.codef.subject.application.dto.response.MultipleAuthResponse;
import io.codef.subject.application.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
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
    public ResponseEntity<String> getHealthCheckResponse(@RequestBody HealthCheckRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {

        // 1. 인증 응답 받기
        MultipleAuthResponse authResponse = easyCodefHealthService.requestHealthSimpleAuth(request);

        // 2. 2023, 2022, 2021, 2020년에 대한 비동기 요청
        Thread.sleep(700);
        log.warn("2023 Request");
        CompletableFuture<String> future2023 = CompletableFuture.supplyAsync(() -> {
            try {
                return easyCodefHealthService.requestHealthCheckResponse(authResponse, request, "2023");
            } catch (UnsupportedEncodingException | JsonProcessingException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

//        log.warn("2022 Request");
//        Thread.sleep(300);
//        CompletableFuture<String> future2022 = CompletableFuture.supplyAsync(() -> {
//            try {
//                return easyCodefHealthService.requestHealthCheckResponse(authResponse, request, "2022");
//            } catch (UnsupportedEncodingException | JsonProcessingException | InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        log.warn("2021 Request");
//        Thread.sleep(300);
//        CompletableFuture<String> future2021 = CompletableFuture.supplyAsync(() -> {
//            try {
//                return easyCodefHealthService.requestHealthCheckResponse(authResponse, request, "2021");
//            } catch (UnsupportedEncodingException | JsonProcessingException | InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        log.warn("2020 Request");
//        Thread.sleep(300);
//        CompletableFuture<String> future2020 = CompletableFuture.supplyAsync(() -> {
//            try {
//                return easyCodefHealthService.requestHealthCheckResponse(authResponse, request, "2020");
//            } catch (UnsupportedEncodingException | JsonProcessingException | InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });

        //15초 대기
        log.warn("15초 남음");
        Thread.sleep(10000);
        log.warn("5초 남음");
        Thread.sleep(3000);
        log.warn("2초 남음");
        Thread.sleep(2000);

        log.warn("인증 요청");
        String result2024 = easyCodefHealthService.requestCertification(authResponse, request);

        // 모든 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf(future2023);
//        CompletableFuture.allOf(future2023, future2022, future2021, future2020).join();

        // 결과 결합
        String result = result2024 + future2023.get();

        return ResponseEntity.ok(future2023.get());
    }
}
