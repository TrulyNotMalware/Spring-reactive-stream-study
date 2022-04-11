package com.spring.devplt;

import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import com.spring.devplt.services.Services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ServicesTest { //Service Unit test
    Services service;

//    @MockBean //협력자를 Import
//    private UserRepository userRepository;
    // 이 코드는 아래 코드와 동일
    @BeforeEach
    void setUp(){
        UserRepository userRepository = mock(UserRepository.class);
        //Test 데이터 정의
        User sampleUser = new User("test","1234","testUser",true,null);

        //협력자와 상호작용을 정의.
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(sampleUser));

        //생성자 주입. Mock 협력자를 주입.
        service = new Services(userRepository,null,null,null);
    }

    @Test
    void createNewUserTest(){
        // 이런 User 객체를, 테스트용으로 사용한다.
        this.service.createNewUser("test","testUser",true)
        .as(StepVerifier::create).expectNextMatches(user -> {
                    assertThat(user.getPwd()).containsExactly("1234");
                    return true;
                }).verifyComplete();
    }

}
