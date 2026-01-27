package com.livepix.livepix.alerts;

import java.math.BigDecimal;
import java.time.Instant;

public record AlertEvent(
        String nome,
        String status,
        String mensagem,
        BigDecimal valor,
        long paymentId,
        Instant em

) {}
