package com.sd.service.todolist.boundary;

import static com.sd.service.todolist.entity.TaskStatus.DONE;
import static com.sd.service.todolist.entity.TaskStatus.NOT_DONE;
import static com.sd.service.todolist.entity.TaskStatus.PAST_DUE;
import static com.sd.service.todolist.utils.PersonHelper.PERSON_ID;
import static com.sd.service.todolist.utils.PersonHelper.createPerson;
import static com.sd.service.todolist.utils.TaskHelper.TASKS_DETAIL_FILE_PATH;
import static com.sd.service.todolist.utils.TaskHelper.TASK_ID;
import static com.sd.service.todolist.utils.TaskHelper.createTask;
import static com.sd.service.todolist.utils.TaskHelper.createTasks;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.sd.service.todolist.Application;
import com.sd.service.todolist.entity.Person;
import com.sd.service.todolist.entity.Task;
import com.sd.service.todolist.repository.PersonRepository;
import com.sd.service.todolist.repository.TaskRepository;
import com.sd.service.todolist.service.TasksService;
import com.sd.service.todolist.util.ResourceLocator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Optional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
class TasksControllerIT {
    public static final URI RESOURCE_URI = URI.create("https://todo-list.co/tasks/1");

    @LocalServerPort
    private int port;

    private String taskDetail;

    private static String TASK_PATH = "/tasks";

    private static String TASK_BY_ID_PATH = "/tasks/{id}";

    private static String TASK_BY_PERSON_PATH = "/tasks/person/{id}";

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private TasksService taskService;

    @MockBean
    private ResourceLocator resourceLocator;

    @InjectMocks
    private TaskController controller;

    @BeforeEach
    public void setUp() throws IOException {
        controller = new TaskController(taskService, resourceLocator);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        taskDetail = Files.readString(TASKS_DETAIL_FILE_PATH);
    }

    @Test
    void createTaskShouldSucceed() {
        when(personRepository.findById(PERSON_ID))
                .thenReturn(Optional.of(createPerson()));
        when(taskRepository.save(any(Task.class)))
                .thenReturn(createTask(NOT_DONE));
        when(resourceLocator.getResourceUri(eq(TaskController.class), eq("getTask"), eq(TASK_ID)))
                .thenReturn(RESOURCE_URI);

        given()
                .contentType(ContentType.JSON)
                .body(taskDetail)
                .when()
                .post(TASK_PATH)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .header("Location", is(RESOURCE_URI.toString()));
    }

    @Test
    void createTaskShouldFailWithNotFoundPerson() {
        when(personRepository.findById(PERSON_ID))
                .thenReturn(Optional.empty());

        given()
                .contentType(ContentType.JSON)
                .body(taskDetail)
                .when()
                .post(TASK_PATH)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("description", is("404 NOT_FOUND"))
                .body("message", is(notNullValue()));
    }


    @Test
    void getTaskByIdShouldSucceed() {
        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.of(createTask(NOT_DONE)));

        given()
                .pathParam("id", TASK_ID)
                .when()
                .get(TASK_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", is(TASK_ID));
    }

    @Test
    void getAllTaskByPersonShouldSucceed() {
        when(personRepository.findById(PERSON_ID))
                .thenReturn(Optional.of(createPerson()));
        when(taskRepository.findByPerson(any(Person.class)))
                .thenReturn(createTasks());

        given()
                .pathParam("id", PERSON_ID)
                .when()
                .get(TASK_BY_PERSON_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(3))
                .body("[0].status", is(NOT_DONE.toString()))
                .body("[1].status", is(DONE.toString()))
                .body("[2].status", is(PAST_DUE.toString()));
    }

    @Test
    void getOnlyDoneTaskByPersonShouldSucceed() {
        when(personRepository.findById(PERSON_ID))
                .thenReturn(Optional.of(createPerson()));
        when(taskRepository.findByPerson(any(Person.class)))
                .thenReturn(createTasks());

        given()
                .pathParam("id", PERSON_ID)
                .queryParam("onlyDone", true)
                .when()
                .get(TASK_BY_PERSON_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(1))
                .body("[0].status", is(DONE.toString()));
    }
}
