package com.livepix.livepix.mercadopago.client;

import com.livepix.livepix.mercadopago.dto.MpCreatePaymentRequest;
import com.livepix.livepix.mercadopago.dto.MpPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MercadoPagoClient {

    private final WebClient mercadoPagoWebClient;

    public MpPaymentResponse criarPagamentoPix(MpCreatePaymentRequest request) {
        String idempotencyKey = UUID.randomUUID().toString();

        try {
            return mercadoPagoWebClient.post()
                    .uri("/v1/payments")
                    .header("X-Idempotency-Key", idempotencyKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(MpPaymentResponse.class)
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

    public MpPaymentResponse buscarPagamentoPorId(long paymentId) {
        try {
            return mercadoPagoWebClient.get()
                    .uri("/v1/payments/{id}", paymentId)
                    .retrieve()
                    .bodyToMono(MpPaymentResponse.class)
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
