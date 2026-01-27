package com.livepix.livepix.webhook.controller;
import com.livepix.livepix.alerts.AlertEvent;
import com.livepix.livepix.alerts.service.AlertsPublisher;
import com.livepix.livepix.mercadopago.client.MercadoPagoClient;
import com.livepix.livepix.mercadopago.dto.MpPaymentResponse;
import com.livepix.livepix.pagamentos.model.PagamentoStatus;
import com.livepix.livepix.pagamentos.repository.PagamentosPixRepository;
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
public class MercadoPagoWebhookController {

    private static final Logger log = LoggerFactory.getLogger(MercadoPagoWebhookController.class);
    private final AlertsPublisher alertsPublisher;
    private final ObjectMapper objectMapper;
    private final MercadoPagoClient mercadoPagoClient;
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
        MpPaymentResponse mpPayment = mercadoPagoClient.buscarPagamentoPorId(paymentId);

        String status = mpPayment.status() == null ? "" : mpPayment.status().toLowerCase();
        log.info("Webhook MP paymentId={} status={}", paymentId, status);

        repository.findByMercadoPagoId(paymentId).ifPresent(p -> {
            if (p.getStatus() == PagamentoStatus.APROVADO) return;

            if ("approved".equals(status)) {
                p.marcarAprovado();
                alertsPublisher.publish(new AlertEvent(
                        p.getNomeDoador(),
                        p.getStatus().name(),
                        p.getMensagem(),
                        p.getValor(),
                        paymentId,
                        Instant.now()
                ));
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