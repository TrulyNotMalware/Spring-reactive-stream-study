package com.spring.devplt.repository;

import com.spring.devplt.models.HttpTraceWrapper;
import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

// HttpTrace 자체가 Non Blocking 을 지원하지를 않는다. 따라서 비동기로 굳이 짤 필요도 없다.
public interface BlockHttpTraceWrapperRepository extends Repository<HttpTraceWrapper, String> {

    Stream<HttpTraceWrapper> findAll();
    void save(HttpTraceWrapper traceWrapper);
}
