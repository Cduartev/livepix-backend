package com.livepix.livepix.configuracao;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(PropriedadesMercadoPago.class)
public class ConfiguracaoMercadoPago {

    @Bean
    public WebClient mercadoPagoWebClient(PropriedadesMercadoPago props) {
        return WebClient.builder()
                .baseUrl(props.baseUrl())
                .defaultHeader("Authorization", "Bearer " + props.accessToken())
                .build();
    }
}
