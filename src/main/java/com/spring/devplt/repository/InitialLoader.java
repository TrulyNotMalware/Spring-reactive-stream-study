package com.spring.devplt.repository;

import com.spring.devplt.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class InitialLoader {

    //Blocking 방식의 Repository 를 사용하는것은, Reactive 의 구현에서 그렇게 좋은 방법이 아님.
//    @Bean
//    CommandLineRunner initialize(BlockUserRepository repository){
//        return args -> {
//            repository.save(new User("root","1234","Admin"));
//        };
//    }
    // Non Block 방식의 Repository 에서 blocking 기능을 구현한다.
    // 이러면 누군가 Blocking 방식의 Repo를 쓸 일도 없다.
//    @Bean
//    CommandLineRunner initializeOnReativeWay(MongoOperations mongo){
//        return args -> {
//            mongo.save(new User("root","1234","Admin",true, null));
//        };
//    }
}
