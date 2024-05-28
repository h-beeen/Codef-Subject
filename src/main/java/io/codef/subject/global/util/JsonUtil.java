package io.codef.subject.global.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class JsonUtil {
    public ResponseEntity<String> serializeResponse(String response) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
