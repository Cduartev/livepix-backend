package com.livepix.livepix.alertas.controlador;

import com.livepix.livepix.alertas.servico.PublicadorAlertas;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class ControladorAlertas {

    private final PublicadorAlertas publisher;

    @GetMapping("/stream")
    public SseEmitter stream() {
        return publisher.subscribe();
    }

}
