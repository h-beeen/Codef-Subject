package io.codef.subject.application.dto.request;

public record HealthCheckRequest(
        String userName,
        String phoneNo,
        String identity,
        String telecom
) {
}
