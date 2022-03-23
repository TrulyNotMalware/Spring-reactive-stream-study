package com.spring.devplt.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequestMapping(path="/api/test")
public class TemplateController {

    @GetMapping(value="/template")
    Mono<String> test_return(){
        return Mono.just("defaultFallBack");
    }

}
