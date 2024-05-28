package io.codef.subject.application.dto.request;

import java.util.List;

public record HealthCheckRequest(
        String userName,
        String phoneNo,
        String identity,
        String telecom,
        List<String> targetYears
) {
}
