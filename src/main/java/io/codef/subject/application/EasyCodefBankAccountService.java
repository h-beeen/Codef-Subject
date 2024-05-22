package io.codef.subject.application;

import io.codef.api.EasyCodef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EasyCodefBankAccountService {

    private final EasyCodef easyCodef;
}
