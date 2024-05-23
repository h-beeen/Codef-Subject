package io.codef.subject.application.dto;

public record TransactionRequest(
        String organizationCode,
        String withdrawAccountNo
) {
}
