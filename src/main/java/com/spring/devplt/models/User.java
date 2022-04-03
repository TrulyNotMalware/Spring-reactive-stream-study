package com.spring.devplt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
public class User {

    private @Id String id;
    private String pwd;
    private String name;
    private boolean isAvailable;
    private Date last_login;
}
