package io.codef.subject.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.subject.application.AuthenticationService;
import io.codef.subject.application.dto.request.ConnectedIdRequest;
import io.codef.subject.global.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ConnectedIdController extends JsonUtil {

    private final AuthenticationService authenticationService;

    /**
     * 커넥티드 아이디 신규 생성 API
     */
    @PostMapping("/connectedId")
    public ResponseEntity<String> initConnectedId(@RequestBody ConnectedIdRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String result = authenticationService.initConnectedId(request);
        return serializeResponse(result);
    }

    /**
     * 커넥티드 아이디에 신규 계정 추가
     */
    @PostMapping("/connectedId/add")
    public ResponseEntity<String> addAccountOnConnectedId(@RequestBody ConnectedIdRequest request) {
        String result = authenticationService.addAccountOnConnectedId(request);
        return serializeResponse(result);
    }

    /**
     * 커넥티드 아이디 전체 조회
     */
    @GetMapping("/connectedId")
    public ResponseEntity<String> addAccountOnConnectedId() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String result = authenticationService.getConnectedIds();
        return serializeResponse(result);
    }
}
