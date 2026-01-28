package com.livepix.livepix.pagamentos.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pagamento_pix")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentosPix {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, length = 100)
    private String nomeDoador;

    @Column(unique = true)
    private Long mercadoPagoId;

    @Column(nullable = false)
    private BigDecimal valor;

    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PagamentoStatus status;

    @Column(nullable = false)
    private Instant criadoEm;

    private Instant pagoEm;

    public void marcarAprovado() {
        this.status = PagamentoStatus.APROVADO;
        this.pagoEm = Instant.now();
    }

}
