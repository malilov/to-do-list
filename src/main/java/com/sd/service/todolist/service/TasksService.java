package com.sd.service.todolist.service;

import com.sd.service.todolist.model.TaskDto;
import com.sd.service.todolist.model.TaskPatchDto;
import java.util.List;

public interface TasksService {

    Integer createTask(TaskDto task);

    TaskDto getTaskById(final Integer id);

    List<TaskDto> getTasksByPerson(final Integer id, Boolean status);

    TaskDto updateTaskPartially(final Integer id, TaskPatchDto status);
}
