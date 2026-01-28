package com.livepix.livepix.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RespostaPagamentoMp(
        Long id,
        String status,
        @JsonProperty("point_of_interaction") PointOfInteraction pointOfInteraction) {
    public record PointOfInteraction(
            @JsonProperty("transaction_data") TransactionData transactionData) {
    }

    public record TransactionData(
            @JsonProperty("qr_code") String qrCode,
            @JsonProperty("qr_code_base64") String qrCodeBase64) {
    }
}
