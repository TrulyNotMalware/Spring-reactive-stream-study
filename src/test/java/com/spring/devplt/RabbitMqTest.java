package com.spring.devplt;

import com.spring.devplt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
@ContextConfiguration
public class RabbitMqTest {

    @Container static RabbitMQContainer container = new RabbitMQContainer("rabbitmq:3.7.25-management-alpine");


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry){
        registry.add("spring.rabbitmq.host", container::getHost);
        registry.add("spring.rabbitmq.port",container::getAmqpPort);
    }

    @Test
    void verifyMessagingThroughAmqp() throws InterruptedException{
        HashMap<String,String> body1 = new HashMap<>();
        body1.put("id","testUserId");
        body1.put("name","testUserName");
        HashMap<String,String> body2 = new HashMap<>();
        body2.put("id","testUserId2");
        body2.put("name","testUserName2");
        this.webTestClient.put().uri("/api/amqp/insertNewUser")
                .bodyValue(body1)
                .exchange()
                .expectStatus().isCreated()
                .expectBody();
        Thread.sleep(1500L);

        this.webTestClient.put().uri("/api/amqp/insertNewUser")
                .bodyValue(body2)
                .exchange()
                .expectStatus().isCreated()
                .expectBody();
        Thread.sleep(1500L);

        this.userRepository.findAll()
                .as(StepVerifier::create)
                .expectNextMatches(user -> {
                    assertThat(user.getId()).isEqualTo("testUserId");
                    assertThat(user.getName()).isEqualTo("testUserName");
                    return true;
                })
                .expectNextMatches(user -> {
                    assertThat(user.getId()).isEqualTo("testUserId2");
                    assertThat(user.getName()).isEqualTo("testUserName2");
                    return true;
                }).verifyComplete();
    }
}
