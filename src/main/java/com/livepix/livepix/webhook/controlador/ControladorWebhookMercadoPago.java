package com.livepix.livepix.webhook.controlador;

import com.livepix.livepix.alertas.EventoAlerta;
import com.livepix.livepix.alertas.servico.PublicadorAlertas;
import com.livepix.livepix.mercadopago.cliente.ClienteMercadoPago;
import com.livepix.livepix.mercadopago.dto.RespostaPagamentoMp;
import com.livepix.livepix.pagamentos.modelo.PagamentoStatus;
import com.livepix.livepix.pagamentos.repositorio.PagamentosPixRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@RestController
@RequestMapping("/webhook/mercadopago")
@RequiredArgsConstructor
public class ControladorWebhookMercadoPago {

    private static final Logger log = LoggerFactory.getLogger(ControladorWebhookMercadoPago.class);
    private final PublicadorAlertas alertsPublisher;
    private final ObjectMapper objectMapper;
    private final ClienteMercadoPago mercadoPagoClient;
    private final PagamentosPixRepository repository;

    @PostMapping
    public ResponseEntity<Void> receber(@RequestBody(required = false) byte[] rawBody) throws Exception {
        String body = rawBody == null ? "" : new String(rawBody, StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(body);
        JsonNode data = node.get("data");
        if (data == null || data.get("id") == null) {
            return ResponseEntity.ok().build();
        }

        long paymentId = data.get("id").asLong();
        RespostaPagamentoMp mpPayment = mercadoPagoClient.buscarPagamentoPorId(paymentId);

        String status = mpPayment.status() == null ? "" : mpPayment.status().toLowerCase();
        log.info("Webhook MP paymentId={} status={}", paymentId, status);

        repository.findByMercadoPagoId(paymentId).ifPresent(p -> {
            if (p.getStatus() == PagamentoStatus.APROVADO)
                return;

            if ("approved".equals(status)) {
                p.marcarAprovado();
                alertsPublisher.publish(new EventoAlerta(
                        p.getNomeDoador(),
                        p.getStatus().name(),
                        p.getMensagem(),
                        p.getValor(),
                        paymentId,
                        Instant.now()));
            } else if ("rejected".equals(status)) {
                p.setStatus(PagamentoStatus.REJEITADO);
            } else if ("cancelled".equals(status)) {
                p.setStatus(PagamentoStatus.CANCELADO);
            } else {
                p.setStatus(PagamentoStatus.DESCONHECIDO);
            }

            repository.save(p);
        });

        return ResponseEntity.ok().build();
    }
}
