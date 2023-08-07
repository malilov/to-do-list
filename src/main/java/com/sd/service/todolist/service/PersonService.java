package com.sd.service.todolist.service;
import com.sd.service.todolist.entity.Person;
import com.sd.service.todolist.model.PersonDto;
import java.util.List;

public interface PersonService {

    Integer createPerson(final PersonDto person);

    List<PersonDto> getAllPersons();

    Person getPersonById(Integer id);
}
