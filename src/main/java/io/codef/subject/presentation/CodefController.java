package io.codef.subject.presentation;

import io.codef.subject.application.CodefBankAccountService;
import io.codef.subject.application.dto.request.TransactionRequest;
import io.codef.subject.global.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/code")
public class CodefController extends JsonUtil {

    private final CodefBankAccountService codefBankAccountService;


    /**
     * @param organizationCode Bearer Token & ConnectedId 기반 은행 계좌 조회
     */
    @PostMapping("/bank/accountList")
    public ResponseEntity<String> getBankAccountResponse(@RequestParam String organizationCode) {
        String result = codefBankAccountService.getBankAccountResponses(organizationCode);
        return serializeResponse(result);
    }

    /**
     * @param request BearerToken & ConnectedId 기반 수시 입출 내역 조회
     */
    @PostMapping("/bank/transaction")
    public ResponseEntity<String> getBankTransactionResponse(@RequestBody TransactionRequest request) {
        String result = codefBankAccountService.getBankTransactionResponses(request);
        return serializeResponse(result);
    }
}
