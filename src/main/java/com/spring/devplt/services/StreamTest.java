package com.spring.devplt.services;


import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@ToString
public class StreamTest {

    Flux<TestModel> getInfo(){
        return Flux.just(
                new TestModel("id1","pwd1",false),
                new TestModel("id2","pwd2",false),
                new TestModel("id3","pwd3",false)
        );
    }
}
