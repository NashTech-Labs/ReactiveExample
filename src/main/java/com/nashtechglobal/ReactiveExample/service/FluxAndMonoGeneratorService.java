package com.nashtechglobal.ReactiveExample.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;


public class FluxAndMonoGeneratorService {

    /**
     * @return Flux.just(" alex ", " ben ", " chloe ");
     */
    public Flux<String> namesFlux() {
        List<String> namesList = List.of("alex", "ben", "chloe");
        return Flux.fromIterable(namesList); // coming from a db or remote service
    }

    public Flux<String> namesFluxImmutability() {
        List<String> namesList = List.of("alex", "ben", "chloe");
        //return Flux.just("alex", "ben", "chloe");
        var namesFlux = Flux.fromIterable(namesList);
        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }


    public Flux<String> namesFluxMap(int stringLength) {
        List<String> namesList = List.of("alex", "ben", "chloe");
        return Flux.fromIterable(namesList)
                .map(String::toUpperCase)
                .delayElements(Duration.ofMillis(500))
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-" + s)
                .doOnNext(name -> {
                    System.out.println("name is : " + name);
                })
                .doOnSubscribe(s -> {
                    System.out.println("Subscription  is : " + s);
                })
                .doOnComplete(() -> {
                    System.out.println("Completed sending all the items.");
                })
                .doFinally((signalType) -> {
                    System.out.println("value is : " + signalType);
                })
                .defaultIfEmpty("default");
    }

    public Mono<String> namesMono() {
        return Mono.just("alex");
    }

    public Mono<String> namesMonoMapFilter(int stirngLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stirngLength)
                .defaultIfEmpty("default");

    }


    /**
     * @param stringLength
     */
    public Flux<String> namesFluxFlatmap(int stringLength) {
        List<String> namesList = List.of("alex", "ben", "chloe");// a, l, e , x
        return Flux.fromIterable(namesList)
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString);
    }

    public Flux<String> namesFluxFlatmapAsync(int stringLength) {
        List<String> namesList = List.of("alex", "ben", "chloe");// a, l, e , x
        return Flux.fromIterable(namesList)
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringWithDelay);


    }

    public Flux<String> namesFluxConcatmap(int stringLength) {
        List<String> namesList = List.of("alex", "ben", "chloe");// a, l, e , x
        return Flux.fromIterable(namesList)
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .concatMap(this::splitStringWithDelay);
    }

    public Mono<List<String>> namesMonoFlatmap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono); //Mono<List of A, L, E  X>
    }

    public Flux<String> namesMonoFlatmapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .flatMapMany(this::splitStringWithDelay);
    }

    private Mono<List<String>> splitStringMono(String s) {
        String[] charArray = s.split("");
        return Mono.just(List.of(charArray))
                .delayElement(Duration.ofSeconds(1));
    }


    public Flux<String> namesFluxTransform(int stringLength) {
        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
        List<String> namesList = List.of("alex", "ben", "chloe");// a, l, e , x
        return Flux.fromIterable(namesList)
                .transform(filterMap)
                .flatMap(this::splitString)
                .defaultIfEmpty("default");
    }


    public Flux<String> namesFluxTransformConcatWith(int stringLength) {
        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-" + s);

        var namesList = List.of("alex", "ben", "chloe"); // a, l, e , x
        var flux1 = Flux.fromIterable(namesList)
                .transform(filterMap);
        var flux2 = flux1.concatWith(Flux.just("anna")
                .transform(filterMap));
        return flux2;
    }

    public Mono<String> nameDefaultIfEmpty() {
        return Mono.<String>empty() // db or rest call
                .defaultIfEmpty("Default");
    }


    public Mono<String> nameSwitchIfEmpty() {
        Mono<String> defaultMono = Mono.just("Default");
        return Mono.<String>empty() // db or rest call
                .switchIfEmpty(defaultMono);
    }

    // "A", "B", "C", "D", "E", "F"
    public Flux<String> exploreConcat() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return Flux.concat(abcFlux, defFlux);
    }

    public Flux<String> exploreConcatWithMono() {
        var aMono = Mono.just("A");
        var bMono = Flux.just("B");
        return aMono.concatWith(bMono);
    }

    // "A", "D", "B", "E", "C", "F"
    // Flux is subscribed early
    public Flux<String> exploreMerge() {
        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return Flux.merge(abcFlux, defFlux).log();
    }

    // "A", "D", "B", "E", "C", "F"
    // Flux is subscribed early
    public Flux<String> exploreMergeWith() {
        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> exploreMergeWithMono() {
        var aMono = Mono.just("A");
        var bMono = Flux.just("B");
        return aMono.mergeWith(bMono);
    }

    // AD, BE, FC
    public Flux<String> exploreZip() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E", "F");
        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second);
    }

    // AD14, BE25, CF36
    public Flux<String> exploreZip_1() {

        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        var flux3 = Flux.just("1", "2", "3");
        var flux4 = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux, flux3, flux4)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4());


    }

    public Flux<String> exploreZip_2() {
        Mono<String> aMono = Mono.just("A");
        Mono<String> bMono = Mono.just("B");
        return Flux.zip(aMono, bMono, (first, second) -> first + second);
    }

    // AD, BE, CF
    public Flux<String> exploreZipWith() {

        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second);


    }

    public Mono<String> exploreZipWithMono() {

        var aMono = Mono.just("A");

        var bMono = Mono.just("B");

        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1() + t2.getT2());

    }

    /***
     * ALEX -> FLux(A,L,E,X)
     * @param name
     * @return
     */
    private Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    private Flux<String> splitStringWithDelay(String name) {
        var delay = new Random().nextInt(1000);
        var charArray = name.split("");
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }
}