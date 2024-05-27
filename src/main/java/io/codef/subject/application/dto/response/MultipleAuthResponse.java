package io.codef.subject.application.dto.response;

public record MultipleAuthResponse(
        String id,
        String jobIndex,
        String threadIndex,
        String transactionId,
        String timestamp
) {
}
