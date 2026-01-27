package com.livepix.livepix.pagamentos.service;

import com.livepix.livepix.config.MercadoPagoProperties;
import com.livepix.livepix.mercadopago.client.MercadoPagoClient;
import com.livepix.livepix.mercadopago.dto.MpCreatePaymentRequest;
import com.livepix.livepix.mercadopago.dto.MpPaymentResponse;
import com.livepix.livepix.pagamentos.model.PagamentoStatus;
import com.livepix.livepix.pagamentos.model.PagamentosPix;
import com.livepix.livepix.pagamentos.repository.PagamentosPixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PixChargeService {

    private final MercadoPagoClient mercadoPagoClient;
    private final PagamentosPixRepository repository;
    private final MercadoPagoProperties props;

    public MpPaymentResponse criarCobranca(
            String nome,
            BigDecimal valor,
            String mensagem
    ) {
        String desc = (mensagem == null || mensagem.isBlank())
                ? "Live Pix"
                : mensagem;

        MpCreatePaymentRequest req = new MpCreatePaymentRequest(
                valor,
                "pix",
                desc,
                new MpCreatePaymentRequest.Payer(props.defaultPayerEmail())
        );

        MpPaymentResponse mp = mercadoPagoClient.criarPagamentoPix(req);

        repository.save(PagamentosPix.builder()
                .mercadoPagoId(mp.id())
                .nomeDoador(nome)
                .valor(valor)
                .mensagem(desc)
                .status(PagamentoStatus.PENDENTE)
                .criadoEm(Instant.now())
                .build());

        return mp;
    }
}