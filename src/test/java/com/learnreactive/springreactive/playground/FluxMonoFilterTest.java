package com.learnreactive.springreactive.playground;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoFilterTest {
    List<String> names = Stream.of("amsterdam", "dubai", "venice", "surat").collect(Collectors.toList());

    @Test
    void filterTest() {
        Flux<String> stringFlux = Flux.fromIterable(names).filter(i -> i.contains("r"))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("amsterdam", "surat")
                .verifyComplete();
    }
}
