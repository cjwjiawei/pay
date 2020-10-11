package top.aolie.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayConfig {

    private String appId;

    private String notifyUrl;

    private String returnUrl;

    private String privateKey;

    private String aliPayPublicKey;


}
