package io.codef.subject.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.subject.application.ConnectedIdService;
import io.codef.subject.application.dto.AddAccountRequest;
import io.codef.subject.application.dto.ConnectedIdRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ConnectedIdController {

    private final ConnectedIdService connectedIdService;

    /**
     * 커넥티드 아이디 신규 생성 API
     */
    @PostMapping("/connectedId")
    public ResponseEntity<Void> initConnectedId(@RequestBody ConnectedIdRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        connectedIdService.initConnectedId(request);
        return ResponseEntity.ok().build();
    }

    /**
     * 커넥티드 아이디에 신규 계정 추가
     */
    @PostMapping("/connectedId/add")
    public ResponseEntity<Void> addAccountOnConnectedId(@RequestBody AddAccountRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        connectedIdService.addAccountOnConnectedId(request);
        return ResponseEntity.ok().build();
    }

    /**
     * 커넥티드 아이디 전체 조회
     */
    @GetMapping("/connectedId")
    public ResponseEntity<String> addAccountOnConnectedId() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String result = connectedIdService.getConnectedIds();
        return ResponseEntity.ok(result);
    }
}
