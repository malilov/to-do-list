package com.sd.service.todolist.utils;

import static com.sd.service.todolist.entity.TaskStatus.DONE;
import static com.sd.service.todolist.entity.TaskStatus.NOT_DONE;
import static com.sd.service.todolist.entity.TaskStatus.PAST_DUE;
import static com.sd.service.todolist.utils.PersonHelper.PERSON_ID;
import static com.sd.service.todolist.utils.PersonHelper.createPerson;

import com.sd.service.todolist.entity.Task;
import com.sd.service.todolist.entity.TaskStatus;
import com.sd.service.todolist.model.PersonDto;
import com.sd.service.todolist.model.TaskDto;
import com.sd.service.todolist.model.TaskPatchDto;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class TaskHelper {
    public static final int TASK_ID = 1;
    public static final LocalDateTime DATE_IN_FUTURE = LocalDateTime.now().plusYears(1L);
    public static final LocalDateTime DATE_IN_PAST = LocalDateTime.now().minusYears(1L);
    public static final Path TASKS_DETAIL_FILE_PATH = Path.of("src/test/resources/task-detail.json");


    public static TaskDto createTaskDto() {
        return TaskDto.builder()
                .description("Plant a tree")
                .dueDate(LocalDateTime.now())
                .person(PersonDto.builder()
                        .id(PERSON_ID)
                        .build())
                .build();
    }

    public static Task createTask(final TaskStatus status) {
        return Task.builder()
                .id(TASK_ID)
                .status(status)
                .dueDate(java.sql.Timestamp.valueOf(DATE_IN_FUTURE))
                .person(createPerson())
                .description("Plant a tree")
                .build();
    }

    public static Task createExpiredTask(final TaskStatus status) {
        return Task.builder()
                .id(TASK_ID)
                .status(status)
                .dueDate(java.sql.Timestamp.valueOf(DATE_IN_PAST))
                .person(createPerson())
                .description("Plant a tree")
                .build();
    }

    public static Task createSimpleTask() {
        return Task.builder()
                .id(TASK_ID)
                .person(createPerson())
                .build();
    }

    public static List<Task> createTasks() {
        return List.of(createTask(NOT_DONE),
                createTask(DONE),
                createTask(PAST_DUE));
    }

    public static TaskPatchDto createDoneTaskPatchDto(){
        return TaskPatchDto.builder()
                .status(DONE)
                .build();
    }
}
