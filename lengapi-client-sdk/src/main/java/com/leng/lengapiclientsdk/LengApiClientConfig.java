package com.leng.lengapiclientsdk;


import com.leng.lengapiclientsdk.client.LengapiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("lengapi.client")
@Data
@ComponentScan
public class LengApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public LengapiClient lengapiClient() {
        return new LengapiClient(accessKey, secretKey);
    }
}
