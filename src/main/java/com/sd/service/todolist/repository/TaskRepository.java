package com.sd.service.todolist.repository;

import com.sd.service.todolist.entity.Person;
import com.sd.service.todolist.entity.Task;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {

    List<Task> findByPerson(Person person);
}
