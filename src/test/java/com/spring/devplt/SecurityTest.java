package com.spring.devplt;

import com.spring.devplt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class SecurityTest {

    @Autowired
    WebTestClient webTestClient;
    @MockBean
    UserRepository repository;

    @Test
    @WithMockUser(username = "MockUser1", roles = {"ROLE_USER"})
    void isAdminUser(){
        this.webTestClient
                .post().uri("/admin/affordance/users")
                .exchange().expectStatus().isForbidden();
    }
}
