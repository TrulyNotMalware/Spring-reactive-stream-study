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

    public JSONObject getNamespaceList(){
        Hooks.onOperatorDebug();
        JSONObject json = new JSONObject();

        client.namespaces()
                .list()
                .getItems()
                .stream()
                .sorted()
                .map(Namespace::getMetadata)
                .peek(ObjectMeta -> { //Testing
                    log.debug(ObjectMeta.getName());
                })
                .map(ObjectMeta::getName)
                .forEach(log::debug);
        json.append("nameSpace",client.namespaces().list().getItems());
        return json;
    }

}