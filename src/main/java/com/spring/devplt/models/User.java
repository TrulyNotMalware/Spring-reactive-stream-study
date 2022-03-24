package com.spring.devplt.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class User {

    private @Id String id;
    private String pwd;
    private String name;

}
