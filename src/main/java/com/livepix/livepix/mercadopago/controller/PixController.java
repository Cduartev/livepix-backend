package com.livepix.livepix.mercadopago.controller;

import com.livepix.livepix.mercadopago.dto.CreatePixChargeHttpRequest;
import com.livepix.livepix.mercadopago.dto.CreatePixChargeHttpResponse;
import com.livepix.livepix.mercadopago.dto.MpPaymentResponse;
import com.livepix.livepix.pagamentos.service.PixChargeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pix")
@RequiredArgsConstructor
public class PixController {

    private final PixChargeService pixChargeService;


    @PostMapping("/cobrar")
    public CreatePixChargeHttpResponse cobrar(
            @Valid @RequestBody CreatePixChargeHttpRequest req
    ) {
        MpPaymentResponse mp = pixChargeService.criarCobranca(
                req.nome(),
                req.valor(),
                req.mensagem()
        );

        String qr = mp.pointOfInteraction() != null &&
                mp.pointOfInteraction().transactionData() != null
                ? mp.pointOfInteraction().transactionData().qrCode()
                : null;

        String qrB64 = mp.pointOfInteraction() != null &&
                mp.pointOfInteraction().transactionData() != null
                ? mp.pointOfInteraction().transactionData().qrCodeBase64()
                : null;

        return new CreatePixChargeHttpResponse(
                mp.id(),
                mp.status(),
                qr,
                qrB64
        );
    }
}