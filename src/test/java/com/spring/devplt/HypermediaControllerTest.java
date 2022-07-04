package com.spring.devplt;


import com.spring.devplt.controllers.HypermediaController;
import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import com.spring.devplt.services.Services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(controllers = HypermediaController.class)
public class HypermediaControllerTest {

    @Autowired private WebTestClient webTestClient;//Web Flux tests.

    @MockBean
    UserRepository userRepository;

    @MockBean
    Services services;

    @BeforeEach
    public void setup(){
        //Test data 준비.
        List<String> roles = Arrays.asList("superuser");
        when(this.userRepository.findById("root"))
                .thenReturn(Mono.just(new User("root","1234","Admin",true,roles,null)));

        this.services = new Services(this.userRepository,null,null,null);
    }

    @Test
    void findUserTest(){
        this.webTestClient
                .get().uri("/api/hypermedia/getUser/root")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    assertThat(stringEntityExchangeResult.getResponseBody()).contains("links");
                    System.out.println(stringEntityExchangeResult.getResponseBody());
                });
    }

}
