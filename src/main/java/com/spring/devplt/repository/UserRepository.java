package com.spring.devplt.repository;

import com.spring.devplt.models.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
    
}
