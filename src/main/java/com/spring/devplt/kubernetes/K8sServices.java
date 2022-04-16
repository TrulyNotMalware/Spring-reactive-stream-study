package com.spring.devplt.kubernetes;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;


import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Hooks;


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