package com.nashtechglobal.ReactiveExample.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxAndMonoGeneratorServiceTest {
    private final FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux();
        StepVerifier.create(namesFlux).expectNext("alex", "ben", "chloe")
//                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesFluxImmutability() {
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFluxImmutability().log();
        StepVerifier.create(stringFlux).expectNext("alex", "ben", "chloe")
//                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesFluxMap() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFluxMap(stringLength).log();
        StepVerifier.create(namesFlux).expectNext("4-ALEX", "5-CHLOE").verifyComplete();
    }

    @Test
    void namesMono() {
        Mono<String> stringMono = fluxAndMonoGeneratorService.namesMono();
        StepVerifier.create(stringMono).expectNext("alex").verifyComplete();
    }

    @Test
    void namesMonoMapFilter() {
        int stringLength = 3;
        Mono<String> stringMono = fluxAndMonoGeneratorService.namesMonoMapFilter(stringLength);
        StepVerifier.create(stringMono).expectNext("ALEX").verifyComplete();

    }

    @Test
    void namesFluxFlatmap() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatmap(stringLength).log();
        StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E").verifyComplete();
    }

    @Test
    void namesFluxFlatmapAsync() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatmapAsync(stringLength).log();
        StepVerifier.create(namesFlux).expectNextCount(9).verifyComplete();
    }

    @Test
    void namesFluxConcatMap() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFluxConcatmap(stringLength).log();
        StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X").expectNextCount(5).verifyComplete();
    }

    @Test
    void namesMono_flatmap() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesMonoFlatmap(stringLength).log();
        StepVerifier.create(namesFlux).expectNext(List.of("A", "L", "E", "X")).verifyComplete();
    }

    @Test
    void namesMonoFlatmapMany() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesMonoFlatmapMany(stringLength).log();
        StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X").verifyComplete();
    }

    @Test
    void namesFluxTransform() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransform(stringLength).log();
        StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X").expectNextCount(5).verifyComplete();
    }

    @Test
    void namesFlux_transform_concatwith() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransformConcatWith(stringLength).log();
        StepVerifier.create(namesFlux)
                .expectNext("4-ALEX", "5-CHLOE", "4-ANNA")
                .verifyComplete();

    }
    @Test
    void nameDefaultIfEmpty() {
        var value = fluxAndMonoGeneratorService.nameDefaultIfEmpty();
        StepVerifier.create(value)
                .expectNext("Default")
                .verifyComplete();
    }

    @Test
    void nameSwitchIfEmpty() {
        var value = fluxAndMonoGeneratorService.nameSwitchIfEmpty();
        StepVerifier.create(value)
                .expectNext("Default")
                .verifyComplete();

    }

    @Test
    void exploreConcat() {
        Flux<String> value = fluxAndMonoGeneratorService.exploreConcat();
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void exploreConcatMono() {
        var value = fluxAndMonoGeneratorService.exploreConcatWithMono();
        StepVerifier.create(value)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void exploreMerge() {
        var value = fluxAndMonoGeneratorService.exploreMerge();
        StepVerifier.create(value)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }
    @Test
    void exploreMergeWith() {
        var value = fluxAndMonoGeneratorService.exploreMergeWith();
        StepVerifier.create(value)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void exploreMergeWithMono() {
        var value = fluxAndMonoGeneratorService.exploreMergeWithMono();
        StepVerifier.create(value)
                .expectNext("A", "B")
                .verifyComplete();

    }
    @Test
    void exploreZip() {
        var value = fluxAndMonoGeneratorService.exploreZip().log();
        StepVerifier.create(value)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }
    @Test
    void exploreZip_1() {
        var value = fluxAndMonoGeneratorService.exploreZip_1().log();
        StepVerifier.create(value)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }

    @Test
    void exploreZip_2() {
        var value = fluxAndMonoGeneratorService.exploreZip_2().log();
        StepVerifier.create(value)
                .expectNext("AB")
                .verifyComplete();
    }

    @Test
    void exploreZipWith() {
        var value = fluxAndMonoGeneratorService.exploreZipWith().log();
        StepVerifier.create(value)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();

    }
    @Test
    void exploreZipWithMono() {
        var value = fluxAndMonoGeneratorService.exploreZipWithMono().log();
        StepVerifier.create(value)
                .expectNext("AB")
                .verifyComplete();

    }
}
