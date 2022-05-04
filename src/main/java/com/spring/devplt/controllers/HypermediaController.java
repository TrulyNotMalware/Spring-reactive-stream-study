package com.spring.devplt.controllers;

import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import com.spring.devplt.services.Services;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.*;
import static org.springframework.hateoas.mediatype.alps.Alps.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/hypermedia")
public class HypermediaController {

    private final Services service;
    private final UserRepository userRepository;

    @GetMapping("/getUser/{id}")
    Mono<EntityModel<User>> findUser(@PathVariable String id){
        HypermediaController controller = methodOn(HypermediaController.class);
        Mono<Link> selfLink = linkTo(controller.findUser(id)).withSelfRel().toMono();

        Mono<Link> aggregateLink = linkTo(controller.findAll()).withRel(IanaLinkRelations.ITEM).toMono();
        return Mono.zip(this.userRepository.findById(id), selfLink, aggregateLink)
                .map(objects -> EntityModel.of(objects.getT1(), Links.of(objects.getT2(), objects.getT3())));
    }

    @GetMapping("/getAllUser")
    Mono<CollectionModel<EntityModel<User>>> findAll(){
        return this.userRepository.findAll()
                .flatMap(user -> findUser(user.getId()))
                .collectList()
                .flatMap(entityModels ->
                        linkTo(methodOn(HypermediaController.class)
                                .findAll()).withSelfRel()
                                .toMono()
                                .map(selfLink -> CollectionModel.of(entityModels, selfLink))
                );
    }
}