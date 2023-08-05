package com.sd.service.todolist.service.impl;

import static com.sd.service.todolist.entity.TaskStatus.DONE;
import static com.sd.service.todolist.entity.TaskStatus.PAST_DUE;
import static java.sql.Timestamp.valueOf;

import com.sd.service.todolist.entity.Task;
import com.sd.service.todolist.entity.TaskStatus;
import com.sd.service.todolist.exception.DataPreconditionException;
import com.sd.service.todolist.exception.StorageException;
import com.sd.service.todolist.mapper.TaskMapper;
import com.sd.service.todolist.model.TaskDto;
import com.sd.service.todolist.model.TaskPatchDto;
import com.sd.service.todolist.repository.TaskRepository;
import com.sd.service.todolist.service.PersonService;
import com.sd.service.todolist.service.TasksService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TasksServiceImpl implements TasksService {

    private PersonService personService;

    private TaskRepository repository;

    private TaskMapper mapper;

    @Autowired
    public TasksServiceImpl(TaskRepository repository, TaskMapper taskMapper, PersonService personService) {
        this.repository = repository;
        this.mapper = taskMapper;
        this.personService = personService;
    }

    @Override
    public Integer createTask(final TaskDto taskDto) {
        taskDto.setCreationDate(LocalDateTime.now());
        taskDto.setStatus(TaskStatus.NOT_DONE);
        Task task = mapper.mapPartially(taskDto)
                .person(personService.getPersonById(taskDto.getPerson().getId()))
                .build();
        task = saveTask(task);
        return task.getId();
    }

    @Override
    public TaskDto getTaskById(Integer id) {
        Task task = getTask(id);
        if (isDueDateExpired(task)) {
            task = updatePastDateStatus(task);
        }
        return mapper.map(task);
    }

    @Override
    public List<TaskDto> getTasksByPerson(final Integer id, Boolean onlyDone) {
        return repository.findByPerson(personService.getPersonById(id))
                .stream()
                .filter(t -> !onlyDone || t.getStatus().equals(DONE))
                .map(t -> mapper.map(updatePastDateStatus(t)))
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto updateTaskPartially(Integer id, TaskPatchDto taskPatch) {
        Task task = getTask(id);
        validateTaskStatus(task);
        updateDoneDate(taskPatch, task);
        task = repository.save(mapper.mapPartially(taskPatch, task));
        return mapper.map(task);
    }

    private static void validateTaskStatus(final Task task) {
        if (task.getStatus().equals(PAST_DUE) || isDueDateExpired(task)) {
            throw new DataPreconditionException("The due date of the task expired. The task can't be changed.");
        }
    }

    private static void updateDoneDate(TaskPatchDto taskPath, Task task) {
        if (taskPath.status() != null && taskPath.status().equals(DONE)) {
            task.setDoneDate(valueOf(LocalDateTime.now()));
        }
    }

    private Task updatePastDateStatus(Task task) {
            task.setStatus(PAST_DUE);
            task = repository.save(task);
        return task;
    }

    private static boolean isDueDateExpired(Task task) {
        return task.getDueDate().toLocalDateTime().isBefore(LocalDateTime.now());
    }

    private Task saveTask(Task task) {
        try {
            return repository.save(task);
        } catch (Exception e) {
            throw new StorageException.UndeterminedException(e, "Unknown error when saving the given task.");
        }
    }

    private Task getTask(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new StorageException.DataNotFoundException("Task '{}' wasn't found.", id));
    }
}
