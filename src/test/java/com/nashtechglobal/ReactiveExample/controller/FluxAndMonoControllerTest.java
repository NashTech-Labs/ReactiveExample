package com.nashtechglobal.ReactiveExample.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
public class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void flux() {
        Flux<Integer> responseBody = webTestClient
                .get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(1,2,3)
                .verifyComplete();
    }

    @Test
    void greetMono() {
        Flux<String> responseBody = webTestClient
                .get()
                .uri("/greet")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext("Hello")
                .verifyComplete();
    }

    @Test
    void fluxApproach_2() {
        webTestClient
                .get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .consumeWith(
                         listEntityExchangeResult -> {
                             var response = listEntityExchangeResult.getResponseBody();
                             assert response.size() == 3;
                         });
    }

    @Test
    void stream() {
        Flux<Long> responseBody = webTestClient
                .get()
                .uri("/stream")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(0L,1L,2L,3L)
                .thenCancel()
                .verify();
    }
}
