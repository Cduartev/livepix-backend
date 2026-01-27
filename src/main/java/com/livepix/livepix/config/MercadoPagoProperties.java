package com.livepix.livepix.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mercadopago")
public record MercadoPagoProperties(
        String baseUrl,
        String accessToken,
        String defaultPayerEmail
) {}
