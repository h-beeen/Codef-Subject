package io.codef.subject.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TargetUrl {
    BANK_ACCOUNT_FULL_URL("https://development.codef.io/v1/kr/bank/p/account/account-list"),
    BANK_ACCOUNT_SHORT_URL("/v1/kr/bank/p/account/account-list"),
    BANK_TRANSACTION_FULL_URL("https://development.codef.io/v1/kr/bank/p/account/transaction-list"),
    BANK_TRANSACTION_SHORT_URL("/v1/kr/bank/p/account/transaction-list"),
    NHIS_HEALTH_CHECKUP_SHORT_URL("/v1/kr/public/pp/nhis-health-checkup/result");

    private final String url;
}
