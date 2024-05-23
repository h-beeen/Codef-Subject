package io.codef.subject.application.dto;

public record AddAccountRequest(
        String connectedId,
        String organizationCode,
        String id,
        String password
) {
}
