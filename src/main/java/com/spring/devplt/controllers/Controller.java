package com.spring.devplt.controllers;


import com.spring.devplt.models.TestModel;
import com.spring.devplt.models.User;
import com.spring.devplt.services.Services;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@ToString
@AllArgsConstructor
@RestController
@RequestMapping(path="/api/test")
public class Controller {

    private final Services service;

    //Constructor 는 Lombok 의 어노테이션 @AllArgsConstructor 를 이용해서 대체한다.
    //Get - Mapping
    @GetMapping(value="/getInfo", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<TestModel> getInfo() {
        return this.service.getInformations();
    }

    @GetMapping(value="/getInfoWithFlags", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public @ResponseBody
    Flux<TestModel> getInfoToChecked(){
        log.debug("/getInfoWithFlags");
        return this.service.getInformations()
                .log("getInformations")
                .map(TestModel -> TestModel.isChecked(TestModel))
                .log("Fin");
    }

    @GetMapping(value="/searchAllUser")
    Flux<User> searchByName(@RequestParam String id, @RequestParam(required = false) String name,
                            @RequestParam boolean isAvailable){
        return this.service.searchWithExampleQuery(id, name,isAvailable);
    }

    @GetMapping(value="/getNamespaceList", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Mono<JSONObject> getNamespaceList(){
        log.debug("getNamespaceLists");
        return this.service.getNamespaceList();
    }
}
