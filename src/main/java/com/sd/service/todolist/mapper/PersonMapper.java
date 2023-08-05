package com.sd.service.todolist.mapper;

import com.sd.service.todolist.model.PersonDto;
import com.sd.service.todolist.entity.Person;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {

    Person map(PersonDto source);

    PersonDto map(Person source);
}
