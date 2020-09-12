package com.learnreactive.springreactive.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxMonoTest {

    @Test
    void fluxTest(){
        Flux<String> stringFlux = Flux.just("spring", "boot", "reactive");
        stringFlux.subscribe(System.out::println);
    }

    @Test
    void monoTest(){
        Mono<String> stringMono = Mono.just("spring").log();
       // stringMono.subscribe(System.out::println);
        StepVerifier.create(stringMono)
                .expectNext("spring")
                .verifyComplete();
    }

    @Test
    void monoErrorTest(){
        StepVerifier.create(Mono.error(new RuntimeException("error occurred")).log())
                .expectError(RuntimeException.class)
                .verify();
    }
}
