package com.livepix.livepix.mercadopago.dto;

public record RespostaHttpCriarCobrancaPix(
        long paymentId,
        String status,
        String qrCode,
        String qrCodeBase64) {
}
