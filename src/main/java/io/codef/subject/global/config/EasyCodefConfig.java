package io.codef.subject.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.EasyCodef;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EasyCodefConfig {

    @Value("${codef.public-key}")
    private String publicKey;

    @Value("${codef.demo-client-id}")
    private String demoClientId;

    @Value("${codef.demo-client-secret}")
    private String demoClientSecret;

    @Bean
    public EasyCodef easyCodef() {
        EasyCodef easyCodef = new EasyCodef();
        easyCodef.setClientInfoForDemo(demoClientId, demoClientSecret);
        easyCodef.setPublicKey(publicKey);
        return easyCodef;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
