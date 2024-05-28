package io.codef.subject.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.subject.application.AuthenticationService;
import io.codef.subject.application.EasyCodefBankAccountService;
import io.codef.subject.application.EasyCodefHealthService;
import io.codef.subject.application.dto.request.HealthCheckRequest;
import io.codef.subject.application.dto.request.TransactionRequest;
import io.codef.subject.application.dto.response.MultipleAuthResponse;
import io.codef.subject.application.dto.response.TokenResponse;
import io.codef.subject.global.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/easy")
public class EasyCodefController extends JsonUtil {

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
        String result = easyCodefBankAccountService.getBankAccountResponse(organizationCode);
        return serializeResponse(result);
    }

    @PostMapping("/bank/transaction")
    public ResponseEntity<String> getBankTransactionResponse(@RequestBody TransactionRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String result = easyCodefBankAccountService.getAccountTransactionResponse(request);
        return serializeResponse(result);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getTokenResponse() throws IOException {
        TokenResponse token = authenticationService.getToken();
        return ResponseEntity.ok(token);
    }

    /**
     * 최근 5년 건강검진 기록 조회
     */
    @PostMapping("/health-check")
    public ResponseEntity<String> getHealthCheckResponse(@RequestBody HealthCheckRequest request) throws Exception {

        // 1. 인증 응답 받기
        MultipleAuthResponse authResponse = easyCodefHealthService.requestHealthSimpleAuth(request);

        List<String> years = Arrays.asList("2023", "2022", "2021", "2020");
        List<CompletableFuture<String>> futures = requestHealthCheckResponses(authResponse, request, years);

        Thread.sleep(15000);
        String result2024 = easyCodefHealthService.requestCertification(authResponse, request);

        String future2023 = futures.get(0).get();
        String future2022 = futures.get(1).get();
        String future2021 = futures.get(2).get();
        String future2020 = futures.get(3).get();

        // 모든 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf().join();

        // 결과 결합
        String result = String.format("%s,%s,%s,%s,%s", result2024, future2023, future2022, future2021, future2020);
        return serializeResponse(result);
    }


    private List<CompletableFuture<String>> requestHealthCheckResponses(MultipleAuthResponse authResponse, HealthCheckRequest request, List<String> years) {
        return years.stream()
                .map(year -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return easyCodefHealthService.requestHealthCheckResponse(authResponse, request, year);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }))
                .toList();
    }
}
