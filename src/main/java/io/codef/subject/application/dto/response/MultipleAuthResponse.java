package io.codef.subject.application.dto.response;

import java.util.HashMap;

public record MultipleAuthResponse(
        String id,
        HashMap<String, Object> data
) {
}
