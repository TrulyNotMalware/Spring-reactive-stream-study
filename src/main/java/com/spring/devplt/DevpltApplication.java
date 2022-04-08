package com.spring.devplt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;


@SpringBootApplication
public class DevpltApplication {
    public static void main(String[] args) {
        BlockHound.install();
        //Template에서 제거할 때는 이렇게.
//        BlockHound.builder()
//                        .allowBlockingCallsInside(
//                                TemplateEngine.class
//                        ).install();
        SpringApplication.run(DevpltApplication.class, args);
    }
}