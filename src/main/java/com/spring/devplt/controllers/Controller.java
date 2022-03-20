package com.spring.devplt.controllers;


import com.spring.devplt.models.TestModel;
import com.spring.devplt.services.Services;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@ToString
@AllArgsConstructor
@RestController
public class Controller {
    private final Services service;

    //Constructor 는 Lombok 의 어노테이션 @AllArgsConstructor 를 이용해서 대체한다.
    //Get - Mapping
    @GetMapping(value="/getInfo", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<TestModel> getInfo() {
        return this.service.getInformations();
    }

}
