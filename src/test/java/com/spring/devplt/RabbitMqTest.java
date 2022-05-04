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
    void verifyMessage() throws InterruptedException{
//        this.webTestClient.post().uri()
    }
}
