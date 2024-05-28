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
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
     * 국민은행의 경우, 보안카드 이슈로 생년월일 입력 에러 응답중
     */
    @PostMapping("/bank/accountList")
    public ResponseEntity<String> getBankAccountResponse(@RequestParam String organizationCode) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String result = easyCodefBankAccountService.getBankAccountResponse(organizationCode);
        return serializeResponse(result);
    }

    /**
     * 기관코드, 계좌번호 기반 수시입출내역 응답
     */
    @PostMapping("/bank/transaction")
    public ResponseEntity<String> getBankTransactionResponse(@RequestBody TransactionRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String result = easyCodefBankAccountService.getAccountTransactionResponse(request);
        return serializeResponse(result);
    }

    /**
     * Bearer 인증 토큰 요청
     */
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
        MultipleAuthResponse authResponse = easyCodefHealthService.requestSimpleAuthToNhis(request);

        final List<String> years = Arrays.asList("2023", "2022", "2021", "2020");
        List<CompletableFuture<String>> futures = requestHealthCheckResponses(authResponse, request, years);

        Thread.sleep(15000);
        String currentResult = easyCodefHealthService.requestCertification(authResponse, request);

        String lastResult = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining(","));

        CompletableFuture.allOf().join();
        String result = String.format("%s,%s", currentResult, lastResult);
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
