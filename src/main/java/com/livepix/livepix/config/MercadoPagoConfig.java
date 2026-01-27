package com.livepix.livepix.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
@EnableConfigurationProperties(MercadoPagoProperties.class)
public class MercadoPagoConfig {

    @Bean
    public WebClient mercadoPagoWebClient(MercadoPagoProperties props){
        return WebClient.builder()
                .baseUrl(props.baseUrl())
                .defaultHeader("Authorization", "Bearer " + props.accessToken())
                .build();
    }
}
