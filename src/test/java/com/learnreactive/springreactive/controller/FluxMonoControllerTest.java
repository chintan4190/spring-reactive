package com.learnreactive.springreactive.controller;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;

@WebFluxTest
@ExtendWith(SpringExtension.class)
public class FluxMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testFluxEndpointApproach1() {
        Flux<Integer> responseBody = webTestClient.get().uri("/flux")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();
        StepVerifier.create(responseBody).expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    void testFluxApproach2() {
        webTestClient.get().uri("/flux")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class).hasSize(4);
    }

    @Test
    void testFluxApproach3() {
        List<Integer> integers = Arrays.asList(1,2,3,4);
        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient.get().uri("/flux")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();
        List<Integer> responseBody = listEntityExchangeResult.getResponseBody();
        assertEquals(integers, responseBody);
    }
}