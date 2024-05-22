package io.codef.subject.presentation;

import io.codef.subject.application.EasyCodefBankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EasyCodefController {

    private final EasyCodefBankAccountService easyCodefBankAccountService;

    @GetMapping("/")
    public void getEasyCodefBankAccountResponse() {
        easyCodefBankAccountService.getBankAccounts();
    }

}
