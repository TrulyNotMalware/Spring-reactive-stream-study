package com.spring.devplt.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import io.fabric8.kubernetes.api.model.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;

@Slf4j
@Configuration
@EnableConfigurationProperties
public class YamlMapper {

    @Value("classpath:../resources/yamls/service.yaml")
    Resource service;
    @Value("classpath:../resources/yamls/deployment.yaml")
    Resource deployment;
    @Value("classpath:../resources/yamls/serviceAccount.yaml")
    Resource serviceAccount;

    ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());


    public Mono<Service> getServiceTemplate(){
        /*
            [NOTE] Read File methods are not reactive way. yamlReader.readValue can call blocking API.
         */
        return Mono.fromCallable(()->{
                    log.debug("Reading Service Templates.");
                    this.yamlReader.findAndRegisterModules();
                    Service service = null;
                    try {
                        service = this.yamlReader.readValue(this.service.getInputStream(),Service.class);
                    } catch (IOException e) {
                        log.error("Cannot Read resource file. {} : {}",this.service.getFilename(),e.toString());
                        e.printStackTrace();
                    }
                    return service;
                }).log("Reading ServiceTemplate Complete.")
                .subscribeOn(Schedulers.boundedElastic()); // Can use publishOn..?
    }
}
