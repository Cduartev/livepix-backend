package com.livepix.livepix;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControladorHealth {
    @GetMapping("/")
    public String health() {
        return "LivePix API is running!";
    }
}
