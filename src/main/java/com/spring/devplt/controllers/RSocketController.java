package com.spring.devplt.controllers;

import static io.rsocket.metadata.WellKnownMimeType.MESSAGE_RSOCKET_ROUTING;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.parseMediaType;

import com.spring.devplt.models.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

@RestController
@RequestMapping(path="/api/rsocket")
public class RSocketController {

    private final Mono<RSocketRequester> requester;

    public RSocketController(RSocketRequester.Builder builder) {
        this.requester = Mono.just(builder.dataMimeType(APPLICATION_JSON)
                .metadataMimeType(parseMediaType(MESSAGE_RSOCKET_ROUTING.toString()))
                .tcp("localhost",7000))//ConnectTcp is deprecated.
                .retry(5)
                .cache();//Hot Sequence
    }

    @PostMapping(value="/addNewUsers")
    Mono<ResponseEntity<?>> addNewUsersRSocketReqRes(@RequestBody User user){
        return this.requester.flatMap(
                rSocketRequester -> rSocketRequester
                        .route("newUsers.request-response")
                        .data(user)
                        .retrieveMono(User.class))
                .map(user1 -> ResponseEntity.created(
                        URI.create("/addNewUsers")).body(user1));
    }

    @GetMapping(value = "/findUsers", produces = MediaType.APPLICATION_NDJSON_VALUE)
    Flux<User> findUserReqStream(){
        return this.requester.flatMapMany(
                rSocketRequester -> rSocketRequester.route("newUsers.request-stream")
                        .retrieveFlux(User.class)
                        .delayElements(Duration.ofSeconds(1))
        );
    }
}
