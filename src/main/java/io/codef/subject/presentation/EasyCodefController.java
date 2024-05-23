package io.codef.subject.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.subject.application.EasyCodefBankAccountService;
import io.codef.subject.application.dto.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/easy")
public class EasyCodefController {

    private final EasyCodefBankAccountService easyCodefBankAccountService;

    /**
     * 보유계좌 조회 (EasyCodef Ver)
     * 국민은행 (0004) / 우리은행 (0020) 커넥티드 아이디 등록 완료
     * request : GET] localhost:8080/api/v1/easy/bank?organizationCode=0020
     */
    @GetMapping("/bank/accountList")
    public ResponseEntity<String> getBankAccountResponse(@RequestParam String organizationCode) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return ResponseEntity.ok(easyCodefBankAccountService.getBankAccountResponse(organizationCode));
    }

    @GetMapping("/bank/transaction")
    public ResponseEntity<String> getBankTransactionResponse(@RequestBody TransactionRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return ResponseEntity.ok(easyCodefBankAccountService.getAccountTransactionResponse(request));
    }
}
