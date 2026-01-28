package com.livepix.livepix.pagamentos.servico;

import com.livepix.livepix.configuracao.PropriedadesMercadoPago;
import com.livepix.livepix.mercadopago.cliente.ClienteMercadoPago;
import com.livepix.livepix.mercadopago.dto.RequisicaoCriarPagamentoMp;
import com.livepix.livepix.mercadopago.dto.RespostaPagamentoMp;
import com.livepix.livepix.pagamentos.modelo.PagamentoStatus;
import com.livepix.livepix.pagamentos.modelo.PagamentosPix;
import com.livepix.livepix.pagamentos.repositorio.PagamentosPixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ServicoCobrancaPix {

        private final ClienteMercadoPago mercadoPagoClient;
        private final PagamentosPixRepository repository;
        private final PropriedadesMercadoPago props;

        public RespostaPagamentoMp criarCobranca(
                        String nome,
                        BigDecimal valor,
                        String mensagem,
                        String email) {
                String desc = (mensagem == null || mensagem.isBlank())
                                ? "Live Pix"
                                : mensagem;

                String payerEmail = (email == null || email.isBlank())
                                ? props.defaultPayerEmail()
                                : email;

                RequisicaoCriarPagamentoMp req = new RequisicaoCriarPagamentoMp(
                                valor,
                                "pix",
                                desc,
                                new RequisicaoCriarPagamentoMp.Payer(payerEmail));

                RespostaPagamentoMp mp = mercadoPagoClient.criarPagamentoPix(req);

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
