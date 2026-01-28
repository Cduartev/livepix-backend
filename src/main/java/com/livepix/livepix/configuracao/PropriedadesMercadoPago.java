package com.livepix.livepix.configuracao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mercadopago")
public record PropriedadesMercadoPago(
        String baseUrl,
        String accessToken,
        String defaultPayerEmail) {
}
