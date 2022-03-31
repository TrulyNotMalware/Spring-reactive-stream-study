package com.spring.devplt.repository;

import com.spring.devplt.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class InitialLoader {

    @Bean
    CommandLineRunner initialize(BlockUserRepository repository){
        return args -> {
            repository.save(new User("root","1234","Admin"));
        };
    }
}
