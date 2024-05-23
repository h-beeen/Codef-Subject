package io.codef.subject.application.dto;

public record ConnectedIdRequest(
        String organizationCode,
        String id,
        String password
) {
}
