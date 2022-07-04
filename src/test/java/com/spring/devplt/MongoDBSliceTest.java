package com.spring.devplt;


import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DataMongoTest
public class MongoDBSliceTest {

    //Spring boot 에서도 Autowired 보다는, 생성자 주입을 통해서 빈을 주입할 것을 권장.
    @Autowired
    UserRepository userRepository;

    User testUser;
    Date today;
    SimpleDateFormat date;
    SimpleDateFormat time;

    @BeforeEach
    public void setUp(){
        // 테스트 데이터 준비.
        this.today = new Date();
        this.date = new SimpleDateFormat("yyyy/MM/dd ");
        this.time = new SimpleDateFormat("hh:mm:ss");
        List<String> roles = Arrays.asList("superuser");
        this.testUser = new User("root","admin1234","admin",true,roles, date.format(today)+time.format(today));

    }

    @Test
    public void userRepositorySaveTest(){
        this.userRepository.save(testUser) // Reactive의 검증은 항상 stepVerifier 와 함께.
                .as(StepVerifier::create)
                .expectNextMatches(user -> {
                    assertNotNull(user.getId());
                    assertEquals(user.getName(),"admin");
                    assertEquals(user.getLast_login(),this.date.format(today)+this.time.format(today));
                    // 여기까지 다 패스했으면, 패스.
                    return true;
                })
        .verifyComplete();
    }
}
