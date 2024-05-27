package io.codef.subject.application;

import io.codef.api.EasyCodef;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final EasyCodef easyCodef;

    @Value("${codef.connected-id}")
    private String connectedId;

    
}
