package com.livepix.livepix.mercadopago.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record MpCreatePaymentRequest(
        @JsonProperty("transaction_amount") BigDecimal transactionAmount,
        @JsonProperty("payment_method_id") String paymentMethodId,
        String description,
        Payer payer
) {
    public record Payer(String email) {}
}