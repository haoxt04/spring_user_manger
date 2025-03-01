package vn.edu.hcmuaf.fit.demohello.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private long expiryHour;
    private long expiryDay;
    private String accessKey;
    private String refreshKey;
}

