package com.spring.devplt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class User {

    private @Id String id;
    private String pwd;
    private String name;

}
