package com.spring.devplt.services;


import com.spring.devplt.kubernetes.K8sServices;
import com.spring.devplt.models.TestModel;
import com.spring.devplt.models.User;
import com.spring.devplt.repository.BlockUserRepository;
import com.spring.devplt.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class Services {
    //Repository
    private final UserRepository userRepository;
    private final K8sServices kubectl;


    private Random picker = new Random();
    // Database 라고 가정하고, 정보를 가져왔다고 생각.
    private List<TestModel> infos = Arrays.asList(
            new TestModel("1","1",false),
            new TestModel("2","2",false),
            new TestModel("3","3",false),
            new TestModel("4","4",false)
    );


    //실제 서비스 모델.
    public Flux<TestModel> getInformations(){
        log.debug("GetInfos");
        return Flux.<TestModel> generate(sink -> sink.next(randomPick()))
                .delayElements(Duration.ofMillis(250));
    }

    //정보를 가져올 데이터베이스가 없으므로, 그냥 Random 으로 뽑았다고 가정한다.
    private TestModel randomPick(){
        return this.infos.get(picker.nextInt(infos.size()));
    }


    public Flux<User> searchWithExampleQuery(String id, String name, boolean isAvailable){
        Hooks.onOperatorDebug();

        User user = new User(id, "1234", name, isAvailable, null);

        ExampleMatcher matcher = ( isAvailable ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny())
                .withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("pwd")
                .withIgnorePaths("last_login");

        Example<User> probe = Example.of(user, matcher);
        return userRepository.findAll(probe);
    }

    public Mono<User> createNewUser(String id,String name, boolean isAvailable){
        Hooks.onOperatorDebug();
        // Default Pwd 1234
        User user = new User(id, "1234", name, isAvailable, null);
        return this.userRepository.save(user)
                .doOnError(error -> {
                    log.trace(error.getMessage());
                });
    }

    public Mono<JSONObject> getNamespaceList(){
        JSONObject results = this.kubectl.getNamespaceList();
        log.debug("Results : {}",results);
        return Mono.just(results);
    }
}
