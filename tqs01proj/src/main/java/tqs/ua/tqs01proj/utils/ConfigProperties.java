package tqs.ua.tqs01proj.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// https://www.baeldung.com/configuration-properties-in-spring-boot/
// dizer tbm que é mais correto / recomendado que usar @value -> https://stackoverflow.com/a/32066380
@Configuration
@ConfigurationProperties("external-api")
public class ConfigProperties {

    private String secret01;
    private String id01;
    private String url01;
    private String url02;
    private String secret02;
    private String url03;
    private String secret03;

    public String getUrl03() {
        return url03;
    }

    public String getSecret03() {
        return secret03;
    }

    public void setUrl03(String url03) {
        this.url03 = url03;
    }

    public void setSecret03(String secret03) {
        this.secret03 = secret03;
    }

    public String getUrl02() {
        return url02;
    }

    public void setUrl02(String url02) {
        this.url02 = url02;
    }

    public String getSecret02() {
        return secret02;
    }

    public void setSecret02(String secret02) {
        this.secret02 = secret02;
    }

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
