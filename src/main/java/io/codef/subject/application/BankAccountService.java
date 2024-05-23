package io.codef.subject.application;

import io.codef.api.EasyCodef;
import io.codef.subject.infra.persistence.ConnectedIdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final EasyCodef easyCodef;
    private final ConnectedIdRepository connectedIdRepository;
}
