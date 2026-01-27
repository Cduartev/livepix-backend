package com.livepix.livepix.alerts.controller;

import com.livepix.livepix.alerts.service.AlertsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertsController {

    private final AlertsPublisher publisher;

            @GetMapping("/stream")
    public SseEmitter stream() {
        return publisher.subscribe();
            }

}
