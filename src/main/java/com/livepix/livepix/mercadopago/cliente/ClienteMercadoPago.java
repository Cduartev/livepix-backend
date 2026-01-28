package com.livepix.livepix.mercadopago.cliente;

import com.livepix.livepix.mercadopago.dto.RequisicaoCriarPagamentoMp;
import com.livepix.livepix.mercadopago.dto.RespostaPagamentoMp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClienteMercadoPago {

    private final WebClient mercadoPagoWebClient;

    public RespostaPagamentoMp criarPagamentoPix(RequisicaoCriarPagamentoMp request) {
        String idempotencyKey = UUID.randomUUID().toString();

        try {
            return mercadoPagoWebClient.post()
                    .uri("/v1/payments")
                    .header("X-Idempotency-Key", idempotencyKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(RespostaPagamentoMp.class)
                    .block();

        } catch (WebClientResponseException ex) {
            System.err.println("========== MERCADO PAGO ERROR ==========");
            System.err.println("Idempotency-Key: " + idempotencyKey);
            System.err.println("Status HTTP: " + ex.getStatusCode());
            System.err.println("Response body:");
            System.err.println(ex.getResponseBodyAsString());
            System.err.println("========================================");
            throw ex;
        }
    }

    public RespostaPagamentoMp buscarPagamentoPorId(long paymentId) {
        try {
            return mercadoPagoWebClient.get()
                    .uri("/v1/payments/{id}", paymentId)
                    .retrieve()
                    .bodyToMono(RespostaPagamentoMp.class)
                    .block();

        } catch (WebClientResponseException ex) {
            System.err.println("========== MERCADO PAGO ERROR (GET) ==========");
            System.err.println("Status HTTP: " + ex.getStatusCode());
            System.err.println("Response body:");
            System.err.println(ex.getResponseBodyAsString());
            System.err.println("==============================================");
            throw ex;
        }
    }
}
