package com.sd.service.todolist.service;

import static com.sd.service.todolist.entity.TaskStatus.NOT_DONE;
import static com.sd.service.todolist.entity.TaskStatus.PAST_DUE;
import static com.sd.service.todolist.utils.PersonHelper.PERSON_ID;
import static com.sd.service.todolist.utils.PersonHelper.createPerson;
import static com.sd.service.todolist.utils.TaskHelper.TASK_ID;
import static com.sd.service.todolist.utils.TaskHelper.createDoneTaskPatchDto;
import static com.sd.service.todolist.utils.TaskHelper.createExpiredTask;
import static com.sd.service.todolist.utils.TaskHelper.createSimpleTask;
import static com.sd.service.todolist.utils.TaskHelper.createTask;
import static com.sd.service.todolist.utils.TaskHelper.createTaskDto;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sd.service.todolist.entity.Person;
import com.sd.service.todolist.entity.Task;
import com.sd.service.todolist.exception.DataPreconditionException;
import com.sd.service.todolist.mapper.TaskMapper;
import com.sd.service.todolist.model.TaskDto;
import com.sd.service.todolist.repository.TaskRepository;
import com.sd.service.todolist.service.impl.TasksServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TasksServiceImplTest {

    private ArgumentCaptor<TaskDto> taskDtoCaptor = ArgumentCaptor.forClass(TaskDto.class);

    private ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

    @Mock
    private PersonService personService;

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskMapper mapper;

    private TasksService tasksService;

    @BeforeEach
    public void setup() {
        tasksService = new TasksServiceImpl(repository, mapper, personService);
    }

    @Test
    void createTaskWithNotDoneStatus() {
        when(personService.getPersonById(PERSON_ID)).thenReturn(createPerson());
        when(mapper.mapPartially(any(TaskDto.class))).thenReturn(Task.builder());
        when(repository.save(any(Task.class))).thenReturn(createSimpleTask());

        tasksService.createTask(createTaskDto());

        verify(mapper).mapPartially(taskDtoCaptor.capture());
        assertThat(taskDtoCaptor.getValue().getStatus(), is(NOT_DONE));
    }

    @Test
    void updateTaskStatusWhenDueDateExpiredOnGetTask() {
        Task task = createExpiredTask(NOT_DONE);
        when(repository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(repository.save(task)).thenReturn(createSimpleTask());

        tasksService.getTaskById(TASK_ID);

        verify(repository).save(taskCaptor.capture());
        assertThat(taskCaptor.getValue().getStatus(), is(PAST_DUE));
    }

    @Test
    void updateTaskStatusWhenDueDateExpiredOnGetTasks() {
        when(personService.getPersonById(PERSON_ID)).thenReturn(createPerson());
        when(repository.findByPerson(any(Person.class))).thenReturn(List.of(createExpiredTask(NOT_DONE)));
        when(repository.save(any(Task.class))).thenReturn(createSimpleTask());

        tasksService.getTasksByPerson(TASK_ID, false);

        verify(repository).save(taskCaptor.capture());
        assertThat(taskCaptor.getValue().getStatus(), is(PAST_DUE));
    }

    @Test
    void throwExceptionWhenModifyingExpiredTask() {
        Task task = createExpiredTask(NOT_DONE);
        when(repository.findById(TASK_ID)).thenReturn(Optional.of(task));

        assertThrows(DataPreconditionException.class, () -> tasksService.updateTaskPartially(TASK_ID, createDoneTaskPatchDto()));

    }

    @Test
    void throwExceptionWhenModifyingPastDueTask() {
        Task task = createTask(PAST_DUE, TASK_ID);
        when(repository.findById(TASK_ID)).thenReturn(Optional.of(task));

        assertThrows(DataPreconditionException.class, () -> tasksService.updateTaskPartially(TASK_ID, createDoneTaskPatchDto()));

    }
}
