package io.codef.subject.application.dto.request;

public record ConnectedIdRequest(
        String connectedId,
        String organizationCode,
        String id,
        String password
) {
}
