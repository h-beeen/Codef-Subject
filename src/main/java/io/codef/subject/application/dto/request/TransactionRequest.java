package io.codef.subject.application.dto.request;

public record TransactionRequest(
        String organizationCode,
        String withdrawAccountNo
) {
}
