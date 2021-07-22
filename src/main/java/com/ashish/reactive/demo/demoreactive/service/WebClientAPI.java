package com.ashish.reactive.demo.demoreactive.service;

import com.ashish.reactive.demo.demoreactive.model.Student;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class WebClientAPI {

    private WebClient webClient;

    public WebClientAPI(){
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));


        this.webClient=WebClient.builder()
                .baseUrl("http://localhost:8080")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private Mono<ResponseEntity> createStudent(){
        return webClient.post()
                .uri("/handlerstudents")
                .body(Mono.just(new Student(null,"Peter",58.8)),Student.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .exchangeToMono(clientResponse -> {
                    System.out.println("here1 "+clientResponse);
                    System.out.println("here2 "+clientResponse.statusCode());
                    if (clientResponse.statusCode()
                            .equals(HttpStatus.CREATED)|clientResponse.statusCode()
                            .equals(HttpStatus.OK)) {
                        return clientResponse.bodyToMono(ResponseEntity.class);
                    } else if (clientResponse.statusCode()
                            .is4xxClientError()) {
                        return Mono.empty();
                    } else {
                        return clientResponse.createException()
                                .flatMap(Mono::error);
                    }
                }).doOnError(throwable -> {
                    System.out.println("Exception here "+throwable);
                })
                .doOnSuccess(responseEntity -> {
                    System.out.println("Exception here "+responseEntity);
                }).doFinally(signalType -> {
                    System.out.println("Finally here "+signalType);

                });


    }
    private Flux<Student> getAllStudent(){
        return webClient.get()
                .uri("/handlerstudents")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .exchangeToFlux(clientResponse -> {
                   // System.out.println("returned here"+clientResponse.body());
                   return clientResponse.bodyToFlux(Student.class);
                });


    }

    private Flux<Student> getAllStudent_(){
        return webClient.get()
                .uri("/handlerstudents")
                /*.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())*/
                .retrieve()
                .bodyToFlux(Student.class)
                .doOnNext(O -> System.out.println("O here is "+O))
                ;


    }

    public static void main(String[] args) {
        WebClientAPI api= new WebClientAPI();
        api.createStudent()
                .thenMany(api.getAllStudent_())
        .subscribe(System.out::println);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
