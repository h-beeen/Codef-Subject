package io.codef.subject.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.subject.application.AuthenticationService;
import io.codef.subject.application.dto.request.AddAccountRequest;
import io.codef.subject.application.dto.request.ConnectedIdRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ConnectedIdController {

    private final AuthenticationService authenticationService;

    /**
     * 커넥티드 아이디 신규 생성 API
     */
    @PostMapping("/connectedId")
    public ResponseEntity<Map> initConnectedId(@RequestBody ConnectedIdRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        Map result = authenticationService.initConnectedId(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 커넥티드 아이디에 신규 계정 추가
     */
    @PostMapping("/connectedId/add")
    public ResponseEntity<String> addAccountOnConnectedId(@RequestBody AddAccountRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String result = authenticationService.addAccountOnConnectedId(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 커넥티드 아이디 전체 조회
     */
    @GetMapping("/connectedId")
    public ResponseEntity<String> addAccountOnConnectedId() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String result = authenticationService.getConnectedIds();
        return ResponseEntity.ok(result);
    }
}
