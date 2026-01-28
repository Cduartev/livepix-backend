package com.livepix.livepix.mercadopago.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RequisicaoHttpCriarCobrancaPix(
        @NotBlank String nome,
        @NotNull @DecimalMin("0.10") BigDecimal valor,
        String mensagem) {
}
