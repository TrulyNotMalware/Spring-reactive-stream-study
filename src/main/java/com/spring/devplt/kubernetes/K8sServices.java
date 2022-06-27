package com.spring.devplt.kubernetes;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;


import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class K8sServices {

    private KubernetesClient client;
    //생성자에서 Kubernetes Builds
    K8sServices(){
        try{
            this.client = new DefaultKubernetesClient();
        }catch(KubernetesClientException e){
            this.client = null;
            log.error("Problem encountered in Kubernetes Client");
            e.printStackTrace();
        }
    }
    //Constructor Overloading
    public K8sServices(KubernetesClient client){
        this.client = client;
    }

    public Service getServiceYamlTemplate(){
        Service service = this.client.services()
                .load(Service.class.getResourceAsStream("../resources/yamls/service.yaml"))
                .get();
        log.debug("Load Service : {}",service.toString());
        return service;
    }

    public Flux<Pod> getPodList(Namespace namespace, String name){
        log.debug("getPodList");
        //if both are empty, return Entire pods
        if(namespace == null && name == null){
            return Mono.just(this.client
                            .pods()
                            .list()
                            .getItems())
                    .flatMapMany(Flux::fromIterable)
                    .doOnNext(pod -> log.info(pod.getMetadata().getName()));
        }
        // Reactive Ways.
        if(name != null){
            return Mono.just(this.client
                            .pods()
                            .inNamespace(name)
                            .list()
                            .getItems())
                    .log("Get item in name")
                    .flatMapMany(Flux::fromIterable)
                    .doOnNext(pod -> log.info(pod.getMetadata().getName()));
        }
        else{
            return Mono.just(this.client
                            .pods()
                            .inNamespace(namespace.getMetadata().getName())
                            .list()
                            .getItems())
                    .log("Get item in namespace")
                    .flatMapMany(Flux::fromIterable)
                    .doOnNext(pod -> log.info(pod.getMetadata().getName()));
        }
    }


    public JSONObject getNamespaceList(){
        Hooks.onOperatorDebug();
        log.debug("getNamespaceLists");
        JSONObject json = new JSONObject();

        this.client.namespaces()
                .list()
                .getItems()
                .stream()
                .sorted()
                .map(Namespace::getMetadata)
                .map(ObjectMeta::getName)
                .forEach(name ->{
                    log.debug("Namespace : {}",name);
                    json.append("nameSpace1",name);
                });
//        json.append("nameSpace",client.namespaces().list().getItems());
        log.debug("Json : {}",json);
        return json;
    }

    public int getPodNumbers(String namespace){
        int PodsNumbers = this.client
                .pods()
                .inNamespace(namespace)
                .list()
                .getItems()
                .size();
        return PodsNumbers;
//                .stream()
//                .map(pod -> {
//                    log.debug(pod.getMetadata().getName());
//                    return pod;
//                })
//                .forEach();

    }

}