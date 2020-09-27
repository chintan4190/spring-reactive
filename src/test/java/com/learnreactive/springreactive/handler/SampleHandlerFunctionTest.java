package com.learnreactive.springreactive.handler;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
class SampleHandlerFunctionTest {

    @Autowired
    WebTestClient webTestClient;


    @Test
    void testFluxEndpointApproach1() {
        Flux<Integer> responseBody = webTestClient.get().uri("/functional/flux")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();
        StepVerifier.create(responseBody).expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

}