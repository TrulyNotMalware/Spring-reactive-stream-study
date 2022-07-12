package com.spring.devplt.controllers;


import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import com.spring.devplt.services.Services;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.*;
import static org.springframework.hateoas.mediatype.alps.Alps.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/hypermedia")
public class AffordanceHypermediaController {
    private final Services service;
    private final UserRepository userRepository;

    static final String ADMIN = "ROLE_ADMIN";

    @PutMapping("/affordance/users/{id}")
    public Mono<ResponseEntity<?>> updateItem(
            @RequestBody Mono<EntityModel<User>> user,
            @PathVariable String id,
            Authentication auth){
        return user
                .map(users->{
                    if(users.getContent() == null) {// Null 을 Return 할 수 있으므로, 주의.
                        log.debug("Put user - EntityModel<User> is null.");
                        return null;
                    }
                    else
                        return users.getContent();
                })
                .map(user1 -> new User(user1.getId(),user1.getPwd(),
                        user1.getName(),user1.isAvailable(),user1.getRoles(),
                        user1.getLast_login()))
                .flatMap(this.userRepository::save)
                .then(findUser(id,auth))
                .map(userEntityModel -> ResponseEntity.noContent()
                        .location(userEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).build());
    }

    @GetMapping("/affordance/users/{id}")
    public Mono<EntityModel<User>> findUser(@PathVariable String id, Authentication auth){
        AffordanceHypermediaController controller = methodOn(AffordanceHypermediaController.class);
        //Set of links
        Mono<Links> allLinks;

        Mono<Link> selfLink = linkTo(controller.findUser(id,auth))
                .withSelfRel()
                .andAffordance(controller.updateItem(null,id,auth))
                .toMono();
        Mono<Link> aggregateLink = linkTo(controller.findAll(auth))
                .withRel(IanaLinkRelations.ITEM)
                .toMono();
        if(auth.getAuthorities().contains(ADMIN)){
            Mono<Link> adminLink = linkTo(controller.adminFindAll(auth))
                    .withRel("AdminLinks").toMono();
            allLinks = Mono.zip(selfLink, aggregateLink, adminLink).map(
                    links -> Links.of(links.getT1(),links.getT2(),links.getT3())
            );
        }else{
            allLinks = Mono.zip(selfLink, aggregateLink).map(
                    links -> Links.of(links.getT1(),links.getT2())
            );
        }
//        return Mono.zip(userRepository.findById(id), selfLink, aggregateLink)
//                .map(objects -> EntityModel.of(objects.getT1(),
//                        Links.of(objects.getT2(), objects.getT3())
//                    )
//                );
        return this.userRepository.findById(id).zipWith(allLinks).map(objects -> EntityModel.of(objects.getT1(), objects.getT2()));
    }

    @GetMapping("/affordance/users")
    Mono<CollectionModel<EntityModel<User>>> findAll(Authentication auth){
        return this.userRepository.findAll()
                .flatMap(user -> findUser(user.getId(),auth))
                .collectList()
                .flatMap(entityModels ->
                        linkTo(methodOn(HypermediaController.class)
                                .findAll()).withSelfRel()
                                .toMono()
                                .map(selfLink -> CollectionModel.of(entityModels, selfLink))
                );
    }

    @PreAuthorize("hasRole('" + ADMIN + "')")
    @PostMapping("/admin/affordance/users")
    Mono<CollectionModel<EntityModel<User>>> adminFindAll(Authentication auth){
        return this.userRepository.findAll()
                .flatMap(user -> findUser(user.getId(),auth))
                .collectList()
                .flatMap(entityModels ->
                        linkTo(methodOn(HypermediaController.class)
                                .findAll()).withSelfRel()
                                .toMono()
                                .map(selfLink -> CollectionModel.of(entityModels,selfLink)));
    }
}
