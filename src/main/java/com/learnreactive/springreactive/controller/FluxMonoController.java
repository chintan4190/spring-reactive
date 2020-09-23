package com.learnreactive.springreactive.controller;

import java.time.Duration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class FluxMonoController {

    @GetMapping("/flux")
    Flux<Integer> flux(){
        return Flux.just(1,2,3,4).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/fluxstream", produces = "application/stream+json")
    Flux<Integer> fluxstream(){
        return Flux.just(1,2,3,4).delayElements(Duration.ofSeconds(1)).log();
    }
}
