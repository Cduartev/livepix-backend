package com.livepix.livepix.mercadopago.controlador;

import com.livepix.livepix.mercadopago.dto.RequisicaoHttpCriarCobrancaPix;
import com.livepix.livepix.mercadopago.dto.RespostaHttpCriarCobrancaPix;
import com.livepix.livepix.mercadopago.dto.RespostaPagamentoMp;
import com.livepix.livepix.pagamentos.servico.ServicoCobrancaPix;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pix")
@RequiredArgsConstructor
public class ControladorPix {

    private final ServicoCobrancaPix pixChargeService;

    @PostMapping("/cobrar")
    public RespostaHttpCriarCobrancaPix cobrar(
            @Valid @RequestBody RequisicaoHttpCriarCobrancaPix req) {
        RespostaPagamentoMp mp = pixChargeService.criarCobranca(
                req.nome(),
                req.valor(),
                req.mensagem());

        String qr = mp.pointOfInteraction() != null &&
                mp.pointOfInteraction().transactionData() != null
                        ? mp.pointOfInteraction().transactionData().qrCode()
                        : null;

        String qrB64 = mp.pointOfInteraction() != null &&
                mp.pointOfInteraction().transactionData() != null
                        ? mp.pointOfInteraction().transactionData().qrCodeBase64()
                        : null;

        return new RespostaHttpCriarCobrancaPix(
                mp.id(),
                mp.status(),
                qr,
                qrB64);
    }
}
