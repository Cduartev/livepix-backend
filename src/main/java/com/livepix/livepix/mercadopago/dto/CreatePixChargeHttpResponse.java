package com.livepix.livepix.mercadopago.dto;

public record CreatePixChargeHttpResponse(
        long paymentId,
        String status,
        String qrCode,
        String qrCodeBase64
) {}
