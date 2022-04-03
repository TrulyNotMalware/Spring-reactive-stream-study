package com.spring.devplt.controllers;


import com.spring.devplt.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping(path="/api/test")
public class TemplateController {

    private final UserRepository userRepo;

    @GetMapping(value="/template")
    Mono<String> test_return(){
        return Mono.just("defaultFallBack");
    }

    @GetMapping
    Mono<Rendering> userTemplate(){
        return Mono.just(Rendering.view("userHomeTeamplte.html")
        .modelAttribute("users",this.userRepo.findAll())
        .build());
    }

}
