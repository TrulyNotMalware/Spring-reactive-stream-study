package com.spring.devplt.services;

import com.spring.devplt.kubernetes.K8sServices;
import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class RabbitMQServices {

    //Repo
    private final UserRepository userRepository;
    //K8s
    private final K8sServices kubectl;
    //Amqp
    private final AmqpTemplate amqpTemplate;

    //RabbitMQ Consumer
    @RabbitListener(ackMode = "MANUAL",
            bindings = @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange("new-user-insert"),
                    key = "new-user-insert"
                    )
            )
    public Mono<Void> consumeInsertUser(User user){
        log.debug("Consumed - user :{}",user);
        return this.userRepository.save(user).then();
    }
}
