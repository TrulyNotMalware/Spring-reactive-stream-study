package com.spring.devplt;

import com.spring.devplt.kubernetes.K8sServices;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

@EnableKubernetesMockClient(crud = true)
public class K8sServicesTest {
    KubernetesMockServer server;
    KubernetesClient client;

    K8sServices services;

    @BeforeEach
    void setUp(){
        //Test 데이터 준비.
        String Namespace = "testNamespaces";
        Pod testPods = new PodBuilder()
                .withNewMetadata()
                .withName("testPods1")
                .withNamespace(Namespace)
                .and()
                .build();
        Pod testPods2 = new PodBuilder()
                .withNewMetadata()
                .withName("testPods2")
                .withNamespace(Namespace)
                .and()
                .build();
        this.client.pods().inNamespace(Namespace).create(testPods);
        this.client.pods().inNamespace(Namespace).create(testPods2);

        // 생성자 주입
        this.services = new K8sServices(client);
    }

    @AfterEach
    public void afterEach() {
        //끝나고 나면 Server destroy.
        this.server.destroy();
    }

    @Test
    public void testPodNumbers(){
        assertEquals(2, this.services.getPodNumbers("testNamespaces"));
    }
}
