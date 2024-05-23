package io.codef.subject.application.dto.request;

public record ConnectedIdRequest(
        String organizationCode,
        String id,
        String password
) {
}
