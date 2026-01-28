package com.livepix.livepix.alertas.servico;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PublicadorAlertas {

    private final Set<SseEmitter> emitters = ConcurrentHashMap.newKeySet();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("ok")
                    .id(String.valueOf(Instant.now().toEpochMilli())));
        } catch (IOException ignored) {
        }

        return emitter;
    }

    /**
     * Publica qualquer payload como JSON no SSE.
     * O Jackson (Spring) serializa o objeto automaticamente.
     */
    public void publish(Object payload) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("pix")
                        .data(payload)
                        .id(String.valueOf(Instant.now().toEpochMilli())));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}
