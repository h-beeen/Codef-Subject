package io.codef.subject.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.subject.application.ConnectedIdService;
import io.codef.subject.application.EasyCodefBankAccountService;
import io.codef.subject.application.dto.ConnectedIdRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EasyCodefController {

    private final EasyCodefBankAccountService easyCodefBankAccountService;
    private final ConnectedIdService connectedIdService;

    /**
     * 커넥티드 아이디 신규 생성 API
     */
    @PostMapping("/connectedId")
    public ResponseEntity<Void> initConnectedId(@RequestBody ConnectedIdRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        connectedIdService.initConnectedId(request);
        return ResponseEntity.ok().build();
    }
}
