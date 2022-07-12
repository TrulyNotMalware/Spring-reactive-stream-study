package com.spring.devplt.repository;

import com.spring.devplt.models.User;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String>, ReactiveQueryByExampleExecutor<User> {

    Mono<User> findByName(String name);
}
