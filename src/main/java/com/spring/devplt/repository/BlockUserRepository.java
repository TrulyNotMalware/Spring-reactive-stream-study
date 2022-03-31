package com.spring.devplt.repository;

import com.spring.devplt.models.User;
import org.springframework.data.repository.CrudRepository;

public interface BlockUserRepository extends CrudRepository<User, String> {
    
}
