package com.livepix.livepix.alertas;

import java.math.BigDecimal;
import java.time.Instant;

public record EventoAlerta(
        String nome,
        String status,
        String mensagem,
        BigDecimal valor,
        long paymentId,
        Instant em

) {
}
