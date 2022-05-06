package com.spring.devplt.controllers;


import com.spring.devplt.services.Services;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/amqp")
public class AmqpController {

    private Services service;

    @PutMapping(value="/insertNewUser")
    Mono<ResponseEntity<?>> insertNewUser(@RequestBody HashMap<String, String> params){
        return this.service.amqpCreateNewUser(params.get("id"),params.get("name"),true);
    }
}
