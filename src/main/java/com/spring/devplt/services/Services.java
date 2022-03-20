package com.spring.devplt.services;


import com.spring.devplt.models.TestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class Services {

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
        return Flux.<TestModel> generate(sink -> sink.next(randomPick()))
                .delayElements(Duration.ofMillis(250));
    }

    //정보를 가져올 데이터베이스가 없으므로, 그냥 Random 으로 뽑았다고 가정한다.
    private TestModel randomPick(){
        return this.infos.get(picker.nextInt(infos.size()));
    }
}
