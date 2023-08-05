package com.sd.service.todolist.mapper;

import static io.micrometer.common.util.StringUtils.isNotEmpty;

import com.sd.service.todolist.entity.Task;
import com.sd.service.todolist.model.TaskDto;
import com.sd.service.todolist.model.TaskPatchDto;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task.TaskBuilder mapPartially(TaskDto source) {
        return Task.builder()
                .description(source.getDescription())
                .dueDate(java.sql.Timestamp.valueOf(source.getDueDate()))
                .creationDate(java.sql.Timestamp.valueOf(source.getCreationDate()))
                .status(source.getStatus());
    }

    public Task mapPartially(TaskPatchDto source, Task task) {
        if(isNotEmpty(source.description())){
            task.setDescription(source.description());
        }
        if(source.status() != null){
            task.setStatus(source.status());
        }
        return task;
    }

    public TaskDto map(Task source) {
        return TaskDto.builder()
                .id(source.getId())
                .doneDate(source.getDoneDate() != null ? source.getDoneDate().toLocalDateTime() : null)
                .creationDate(source.getCreationDate() != null ? source.getCreationDate().toLocalDateTime() : null)
                .dueDate(source.getDueDate() != null ? source.getDueDate().toLocalDateTime() : null)
                .status(source.getStatus())
                .description(source.getDescription())
                .build();
    }

}
