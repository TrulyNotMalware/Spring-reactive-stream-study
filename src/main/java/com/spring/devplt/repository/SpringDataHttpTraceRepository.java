package com.spring.devplt.repository;

import com.spring.devplt.models.HttpTraceWrapper;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpringDataHttpTraceRepository implements HttpTraceRepository {

    private final BlockHttpTraceWrapperRepository repository;

    public SpringDataHttpTraceRepository(BlockHttpTraceWrapperRepository repository ) {
        this.repository = repository;
    }


    @Override
    public List<HttpTrace> findAll() {
        return this.repository.findAll()
                .map(HttpTraceWrapper::getHttpTrace)
                .collect(Collectors.toList());
    }

    @Override
    public void add(HttpTrace trace) {
        this.repository.save(new HttpTraceWrapper(trace));
    }

    @Bean
    HttpTraceRepository SpringDataHttpRepository(BlockHttpTraceWrapperRepository repository){
        return new SpringDataHttpTraceRepository(repository);
    }
}
