package com.spring.devplt.controllers;


import com.spring.devplt.models.TestModel;
import com.spring.devplt.models.User;
import com.spring.devplt.services.Services;
import com.spring.devplt.utils.YamlMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Slf4j
@ToString
@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/test")
public class Controller {

    private Services service;

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

    @PostMapping(value="/insertNewUser")
    Mono<User> insertNewUser(@RequestParam String id,
                             @RequestParam String name){
        log.debug("Insert New user : {}, {}",id,name);
        return this.service.createNewUser(id,name,true);
    }

    @PutMapping(value="/insertNewUser")
    Mono<User> insertNewUser(@RequestBody HashMap<String, String> params){
        //User 객체는 @Data 어노테이션으로, getter & Setter 모두 설정되어 있기 때문에,
        //Json 형식만 완벽하게 맞춰 준다면, @RequestBody 로 User 객체를 받아도 됨.
        return this.service.createNewUser(params.get("id"),params.get("name"),true);
    }

    @PostMapping(value = "/getServiceJson")
    Mono<String> getServiceJson(@RequestBody HashMap<String, String> params){
        log.debug("getServiceJson");
        return this.service.getServiceJson();
    }
}
