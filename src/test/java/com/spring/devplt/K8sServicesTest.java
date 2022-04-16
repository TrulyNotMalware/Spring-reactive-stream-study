package com.spring.devplt;

import com.spring.devplt.kubernetes.K8sServices;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


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
        assertNotNull(this.client.pods()
                .inNamespace("testNamespaces")
                .list());
        assertEquals(2, this.services.getPodNumbers("testNamespaces"));
    }

    @Test
    public void testNamespaceList() {
        //우선 Client 객체가 정상적으로 namespace list 를 출력할 수 있는지부터 확인.
        List<Namespace> kubectlGetNS = this.client.namespaces()
                .list()
                .getItems();
        // Null 값인지 아닌지부터 체크.
        assertNotNull(kubectlGetNS);
        kubectlGetNS
                .stream()
                .map(Namespace::getMetadata)
                .map(ObjectMeta::getName)
                .forEach(System.out::println);
        JSONObject json = this.services.getNamespaceList();

    }
}
