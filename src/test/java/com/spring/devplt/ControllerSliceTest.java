package com.spring.devplt;

import com.spring.devplt.controllers.Controller;
import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import com.spring.devplt.services.Services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// WebFlux Slice test. Junit5 @ExtendsWith(SpringExtension.class) 를 포함하는 어노테이션.
@WebFluxTest(Controller.class)
public class ControllerSliceTest {

    @Autowired
    private WebClient client;

    @MockBean
    Services services;

    private String id;
    private String name;


    @BeforeEach
    public void setup(){
        this.id = "testUsers";
        this.name= "testUserName";

        // 협력자 준비. Controller 의 Slice test 이기 때문에, Service 쪽 로직은 동작 한다는 가정 하에 움직인다.
        // 절대 처음부터 종단테스트를 수행 X
        when(services.createNewUser(this.id,this.name,true))
                .thenReturn(
                        Mono.just(new User(this.id,"1234",this.name,true,null))
                );
    }

    @Test
    public void insertNewUserTest(){
        MultiValueMap<String, String> testParams = new LinkedMultiValueMap<>();
        testParams.add("id", this.id);
        testParams.add("name", this.name);

        this.client.post().uri("/api/test/insertNewUser")
                .body(BodyInserters.fromFormData(testParams))
                .exchangeToMono(clientResponse -> clientResponse.toEntity(String.class))
                .doOnSuccess(stringResponseEntity -> {
                    assertEquals(stringResponseEntity.getStatusCode(), HttpStatus.OK);
                });

    }

}
