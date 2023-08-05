package com.sd.service.todolist.utils;

import com.sd.service.todolist.entity.Person;

public class PersonHelper {

    public static final int PERSON_ID = 1;

    public static Person createPerson(){
        return Person.builder()
                .id(PERSON_ID)
                .email("iiulia@test.de")
                .build();
    }
}
