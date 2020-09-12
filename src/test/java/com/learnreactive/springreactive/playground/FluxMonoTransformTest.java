package com.learnreactive.springreactive.playground;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxMonoTransformTest {

    List<String> names = Stream.of("amsterdam", "dubai", "venice", "surat").collect(Collectors.toList());

    @Test
    void transformUsingMap() {

        Flux<String> stringFlux = Flux.fromIterable(names).map(i -> i.toUpperCase()).log();
        StepVerifier.create(stringFlux)
                .expectNext("AMSTERDAM", "DUBAI", "VENICE", "SURAT")
                .verifyComplete();
    }

    @Test
    void transformUsingMap_length() {
        Flux<String> stringFlux = Flux.fromIterable(names).map(i -> i.length() + "").log();
        StepVerifier.create(stringFlux)
                .expectNext("9", "5", "6", "5")
                .verifyComplete();
    }

    @Test
    void transformUsingMap_length_repeat() {
        Flux<String> stringFlux = Flux.fromIterable(names).map(i -> i.length() + "")
                .repeat(1)
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("9", "5", "6", "5", "9", "5", "6", "5")
                .verifyComplete();
    }

    @Test
    void transformUsingMap_filter() {

        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(i -> i.length() > 5)
                .map(i -> i.toUpperCase()).log();
        StepVerifier.create(stringFlux)
                .expectNext("AMSTERDAM", "VENICE")
                .verifyComplete();
    }

    @Test
    void transformUsingFlatMap(){
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .flatMap( i -> Flux.fromIterable(convertToList(i)))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String i) {
        try {
            Thread.sleep(1000);
            return Arrays.asList(i,"newValue");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Test
    void transformUsingFlatMap_usingParallel(){
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2)
                .flatMap( i -> i.map(this::convertToList).subscribeOn(parallel()))
                .flatMap( i->Flux.fromIterable(i))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    void transformUsingFlatMap_usingParallel_maintainOrder(){
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2)
               // .concatMap( i -> i.map(this::convertToList).subscribeOn(parallel()))
                .flatMapSequential( i -> i.map(this::convertToList).subscribeOn(parallel()))
                .flatMap( i->Flux.fromIterable(i))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }
}
