package com.sd.service.todolist.service.impl;

import com.sd.service.todolist.entity.Person;
import com.sd.service.todolist.exception.StorageException;
import com.sd.service.todolist.mapper.PersonMapper;
import com.sd.service.todolist.model.PersonDto;
import com.sd.service.todolist.repository.PersonRepository;
import com.sd.service.todolist.service.PersonService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;

    private PersonMapper mapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, PersonMapper mapper) {
        this.personRepository = personRepository;
        this.mapper = mapper;
    }

    @Override
    public Integer createPerson(PersonDto personDto) {
        return savePerson(mapper.map(personDto)).getId();
    }

    @Override
    public List<PersonDto> getAllPersons() {
        List<PersonDto> persons = new ArrayList<>();
        personRepository.findAll().forEach(p -> persons.add(mapper.map(p)));
        return persons;
    }

    @Override
    public Person getPersonById(Integer id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new StorageException.DataNotFoundException("Person with ID '{}', wasn't found", id));
    }

    private Person savePerson(Person person) {
        try {
            person = personRepository.save(person);
        } catch (DataIntegrityViolationException e) {
            throw new StorageException.DataIntegrityException(e, "Error saving Person with email: '{}' " +
                    "Please validate: - Email should not exist for other user", person.getEmail());
        } catch (Exception e) {
            throw new StorageException.UndeterminedException(e, "Unknown error when saving Person.");
        }
        return person;
    }


}
