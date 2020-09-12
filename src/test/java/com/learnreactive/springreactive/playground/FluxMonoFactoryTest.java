package com.learnreactive.springreactive.playground;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxMonoFactoryTest {

    List<String> names = Stream.of("amsterdam", "dubai", "venice", "surat").collect(Collectors.toList());

    @Test
    void fluxUsingIterable() {
        Flux<String> stringFlux = Flux.fromIterable(names).log();
        StepVerifier.create(stringFlux)
                .expectNext("amsterdam", "dubai", "venice", "surat")
                .verifyComplete();

    }

    @Test
    void fluxUsingArray() {
        String names[] = new String[]{"amsterdam", "dubai", "venice", "surat"};
        Flux<String> stringFlux = Flux.fromArray(names);
        StepVerifier.create(stringFlux)
                .expectNext("amsterdam", "dubai", "venice", "surat")
                .verifyComplete();
    }

    @Test
    void fluxUsingStreams() {
        Flux<String> stringFlux = Flux.fromStream(names.stream()).log();
        StepVerifier.create(stringFlux)
                .expectNext("amsterdam", "dubai", "venice", "surat")
                .verifyComplete();
    }

    @Test
    void monoUsingEmpty(){
        Mono<String> stringMono = Mono.justOrEmpty(null);
        StepVerifier.create(stringMono.log())
                .verifyComplete();
    }

    @Test
    void monoUsingSupplier(){
        Supplier<String> stringSupplier = () -> "spring";
        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);
        StepVerifier.create(stringMono.log())
                .expectNext("spring")
                .verifyComplete();
    }

    @Test
    void fluxUsingRange(){
        Flux<Integer> integerFlux = Flux.range(1,3);
        StepVerifier.create(integerFlux)
                .expectNext(1,2,3)
                .verifyComplete();
    }
}
