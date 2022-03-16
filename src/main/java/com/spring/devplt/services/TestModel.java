package com.spring.devplt.services;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@AllArgsConstructor
@Getter
public class TestModel {
    private final String id;
    private final String pwd;
    private boolean isChecked = false;

    public static TestModel isChecked(TestModel testmodel){
        TestModel newModel = new TestModel(testmodel.getId(), testmodel.getPwd(), true);
        return newModel;
    }

}