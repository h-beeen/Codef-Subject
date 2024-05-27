package io.codef.subject.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.subject.application.CodefBankAccountService;
import io.codef.subject.application.dto.request.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/code")
public class CodefController {

    private final CodefBankAccountService codefBankAccountService;

    @PostMapping("/bank/accountList")
    public ResponseEntity<Map<String, Object>> getBankAccountResponse(@RequestParam String organizationCode) throws JsonProcessingException, UnsupportedEncodingException {
        return codefBankAccountService.getBankAccountResponses(organizationCode);
    }

    @PostMapping("/bank/transaction")
    public ResponseEntity<Map<String, Object>> getBankTransactionResponse(@RequestBody TransactionRequest request) throws JsonProcessingException, UnsupportedEncodingException {
        return codefBankAccountService.getBankTransactionResponses(request);
    }
}
