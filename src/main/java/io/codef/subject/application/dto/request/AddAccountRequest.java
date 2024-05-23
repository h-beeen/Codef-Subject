package io.codef.subject.application.dto.request;

public record AddAccountRequest(
        String connectedId,
        String organizationCode,
        String id,
        String password
) {
}
