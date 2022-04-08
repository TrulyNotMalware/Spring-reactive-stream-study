package com.spring.devplt;

import com.spring.devplt.repository.UserRepository;
import com.spring.devplt.services.Services;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ServicesTest { //Service Unit test
    Services service;

    @MockBean //협력자를 Import
    private UserRepository userRepository;
    // 이 코드는 아래 코드와 동일
//    @BeforeEach
//    void setUp(){
//        userRepository = mock(UserRepository.class);
//    }
    
}
