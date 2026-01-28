package com.livepix.livepix.alertas.controlador;

import com.livepix.livepix.alertas.EventoAlerta;
import com.livepix.livepix.alertas.servico.PublicadorAlertas;
import com.livepix.livepix.pagamentos.modelo.PagamentoStatus;
import com.livepix.livepix.pagamentos.repositorio.PagamentosPixRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
@Profile("dev")
public class ControladorAprovacaoDev {

    private static final Logger log = LoggerFactory.getLogger(ControladorAprovacaoDev.class);

    private final PagamentosPixRepository repository;
    private final PublicadorAlertas alertsPublisher;

    @PostMapping("/approve/{paymentId}")
    public ResponseEntity<?> approve(@PathVariable long paymentId) {
        return repository.findByMercadoPagoId(Long.valueOf(paymentId))
                .map(p -> {
                    log.info("DEV approve paymentId={} statusAntes={}", paymentId, p.getStatus());

                    if (p.getStatus() != PagamentoStatus.APROVADO) {
                        p.marcarAprovado();
                        repository.save(p);

                        EventoAlerta evt = new EventoAlerta(
                                p.getNomeDoador(),
                                p.getStatus().name(),
                                p.getMensagem(),
                                p.getValor(),
                                paymentId,
                                Instant.now());

                        log.info("DEV publish alert: {}", evt);
                        alertsPublisher.publish(evt);
                    }

                    return ResponseEntity.ok(Map.of(
                            "ok", true,
                            "paymentId", paymentId,
                            "status", p.getStatus().name(),
                            "nome", p.getNomeDoador(),
                            "valor", p.getValor(),
                            "mensagem", p.getMensagem()));
                })
                .orElseGet(() -> {
                    log.warn("DEV approve NOT FOUND paymentId={}", paymentId);
                    return ResponseEntity.status(404).body(Map.of(
                            "ok", false,
                            "error", "Pagamento não encontrado no banco para esse mercadoPagoId",
                            "paymentId", paymentId));
                });
    }
}
