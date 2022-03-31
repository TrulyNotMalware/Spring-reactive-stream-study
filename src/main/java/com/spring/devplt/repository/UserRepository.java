package com.spring.devplt.repository;

import com.spring.devplt.models.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
    @Query(value="{ id = ?0 }")
    Flux<User> ReadUserById(String id);
}
