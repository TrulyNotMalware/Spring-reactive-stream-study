package com.spring.devplt;


import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import com.spring.devplt.services.Services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.HypermediaWebTestClientConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@SpringBootTest
@EnableHypermediaSupport(type = HAL)
@AutoConfigureWebTestClient
public class AffordanceHypermediaControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private UserRepository userRepository;
    @MockBean private Services services;

    @Autowired
    HypermediaWebTestClientConfigurer hypermediaWebTestClientConfigurer;

    @BeforeEach
    void setUp(){

        //Test data 준비.
        List<String> roles = Arrays.asList("ROLE_ADMIN");
        User sampleUser = new User("root","1234","Admin",true,roles,null);

        //상호작용 정의.
        when(this.userRepository.findById("root"))
                .thenReturn(Mono.just(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(sampleUser));

        this.services = new Services(this.userRepository,null,null,null);
        this.webTestClient = this.webTestClient.mutateWith(hypermediaWebTestClientConfigurer);
    }

//    @Test
//    @WithMockUser(username = "testMockUser", roles = {"ROLE_ADMIN"})
//    void test(){
//        RepresentationModel<?> root = this.webTestClient.get()
//                .uri("/affordance/users/root")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(RepresentationModel.class)
//    }
}
