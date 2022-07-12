package com.spring.devplt.security;

import com.spring.devplt.models.User;
import com.spring.devplt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebFluxSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter { DEPRECATED
public class SecurityConfig{
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository repository){
        return username -> {
            Mono<User> user = repository.findByName(username);
            if (user == null) return Mono.empty();
            return user.map(user1 -> {
                log.warn("User.withDefaultPasswordEncoder() is considered unsafe for production and is only intended for sample applications.");
                PasswordEncoder encoder = passwordEncoder();
                return org.springframework.security.core.userdetails.User.withUsername(user1.getName())
                        .passwordEncoder(encoder::encode)
                        .password(user1.getPwd())
                        .authorities(user1.getRoles().toArray(new String[0]))
                        .build();
            });
        };
    }

    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public ReactiveUserDetailsService userDetailsService(UserRepository repository){
//        return username -> repository.findByName(username)
//                .map(user -> User.withDefaultPasswordEncoder())
//                DEPRECATED
//    }

    //방식 변경됨. Extends 해서 구현하지 말고, SecurityFilterChain 을 Bean 으로 등록한다.
    //기존의 configure methods 를 대체한다.
    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                .pathMatchers(HttpMethod.POST,"/admin").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.DELETE,"/**").hasRole("ROLE_ADMIN")
                .anyExchange().authenticated()
                .and()
                .formLogin()
                .and()
                .csrf().disable()
                .httpBasic()).build();
    }

}