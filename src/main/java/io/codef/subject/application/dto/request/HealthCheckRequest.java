package io.codef.subject.application.dto.request;

public record HealthCheckRequest(
        String organization,
        String loginType,
        String loginTypeLevel,
        String userName,
        String phoneNo,
        String identity,
        String inquiryType,
        String type,
        String telecom
) {
}
