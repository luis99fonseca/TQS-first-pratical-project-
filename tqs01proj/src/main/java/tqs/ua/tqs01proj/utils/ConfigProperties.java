package tqs.ua.tqs01proj.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// https://www.baeldung.com/configuration-properties-in-spring-boot
@Configuration
@ConfigurationProperties("external-api")
public class ConfigProperties {

    private String secret01;
    private String id01;
    private String url01;

    public String getUrl01() {
        return url01;
    }

    public void setUrl01(String url01) {
        this.url01 = url01;
    }

    public String getSecret01() {
        return secret01;
    }

    public String getId01() {
        return id01;
    }

    public void setSecret01(String secret01) {
        this.secret01 = secret01;
    }

    public void setId01(String id01) {
        this.id01 = id01;
    }
}
