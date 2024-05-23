package io.codef.subject.global.config;

import io.codef.api.EasyCodef;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EasyCodefConfig {
    private static final String DEMO_CLIENT_ID = "320f7c04-2ac0-4b20-8f85-4c1810c0d757";
    private static final String DEMO_CLIENT_SECRET = "79d0559b-8d31-48f2-a626-a484310e11f3";
    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtZiU31DkbuTxm/lcS2Nr0cHTOm4Snz1F4+wmtS7KEVVWgCsWr4xl4Vzup9OFZPrhkZLIs8AdCS79MCW7xjZ9WklopE7kPGGZBbxIkAo2qbxa0FxYM/C08cPy0kJ48yzi2RhBKcsxaDPMI5K6j84r94TBvhTF9lU2YXqh3FNpGs9T9qqEAsnaBfRqvDcEIjIYE3JNgBnT7edSQ3JelhzJ2SfTAugXGCILE9DbrK7nB+lFho48U7rf2wwJXa3vdl+v14MJ+ppYKSX07Z+5Bj7RLfGazLBuVc0ReIyeNKKCJ9Wx/pE68Wj5ZXIq+4s1W+lwH0iDaVlpjRQEYZ6uGF2LTwIDAQAB";

    @Bean
    public EasyCodef easyCodef() {
        EasyCodef easyCodef = new EasyCodef();
        easyCodef.setClientInfoForDemo(DEMO_CLIENT_ID, DEMO_CLIENT_SECRET);
        easyCodef.setPublicKey(PUBLIC_KEY);
        return easyCodef;
    }
}
