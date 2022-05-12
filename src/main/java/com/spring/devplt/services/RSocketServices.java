package com.spring.devplt.services;

import com.spring.devplt.kubernetes.K8sServices;
import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
public class RSocketServices {
    //Repo
    private final UserRepository userRepository;
    //K8s
    private final K8sServices kubectl;

    private final Sinks.Many<User> userSinks;

    public RSocketServices(UserRepository userRepository, K8sServices kubectl) {
        this.userRepository = userRepository;
        this.kubectl = kubectl;
        this.userSinks = Sinks.many().multicast().onBackpressureBuffer();
    }

    @MessageMapping("newUsers.request-response")
    public Mono<User> saveNewUsersReqRes(User user){
        return this.userRepository.save(user)
                .doOnNext(this.userSinks::tryEmitNext);
    }

    @MessageMapping("newUsers.request-stream")
    public Flux<User> findUserRequestStream(){
        return this.userRepository.findAll()
                .doOnNext(this.userSinks::tryEmitNext);
    }

    @MessageMapping("newUsers.fire-and-forget")
    public Mono<Void> saveNewUserFireAndForget(User user){
        return this.userRepository.save(user)
                .doOnNext(this.userSinks::tryEmitNext)
                .then();
    }
}
