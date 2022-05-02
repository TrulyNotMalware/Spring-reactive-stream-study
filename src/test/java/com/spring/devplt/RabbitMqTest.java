package com.spring.devplt;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
@ContextConfiguration
public class RabbitMqTest {

}
