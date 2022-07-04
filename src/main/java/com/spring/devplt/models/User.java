package com.spring.devplt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;


@Data
@AllArgsConstructor
public class User {

    private @Id String id;
    private String pwd;
    private String name;
    private boolean isAvailable;
    private List<String> roles;
    private String last_login;

}
