package com.spring.devplt;

import com.spring.devplt.controllers.AffordanceHypermediaController;
import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import com.spring.devplt.services.Services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = AffordanceHypermediaController.class)
public class AffordanceControllerTest {
    @Autowired private WebTestClient webTestClient;
    @MockBean
    private UserRepository userRepository;
    @MockBean private Services services;

    @BeforeEach
    public void setup(){
        //Test data 준비.
        List<String> roles = Arrays.asList("superuser");
        User sampleUser = new User("root","1234","Admin",true,roles,null);

        //상호작용 정의.
        when(this.userRepository.findById("root"))
                .thenReturn(Mono.just(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(sampleUser));

        this.services = new Services(this.userRepository,null,null,null);
    }

    @Test
    void findUser(){
        this.webTestClient
                .get().uri("/api/hypermedia/affordance/users/root")
                .accept(MediaTypes.HAL_FORMS_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    assertThat(stringEntityExchangeResult.getResponseBody()).contains("links");
                    System.out.println(stringEntityExchangeResult.getResponseBody());
                });
    }
}
