package com.spring.devplt.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@AllArgsConstructor
public class SimpleServer {
    private final StreamTest testService;

    public Flux<TestModel> getInformations(){
        return this.testService.getInfo()
                .doOnNext(info -> {
                    log.debug(info.getId());
                    log.debug(info.getPwd());
                })
                .doOnError(error -> log.trace(error.getMessage()))
                .doOnComplete(()-> log.info("Finished getInformations"))
                .map(info -> TestModel.isChecked(info));
    }
}