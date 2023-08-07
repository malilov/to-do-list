package com.sd.service.todolist.boundary;

import com.sd.service.todolist.model.TaskDto;
import com.sd.service.todolist.model.patch.TaskPatchDto;
import com.sd.service.todolist.service.TasksService;
import com.sd.service.todolist.util.ResourceLocator;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TasksService taskService;

    private ResourceLocator resourceLocator;

    public TaskController(TasksService taskService, ResourceLocator resourceLocator) {
        this.taskService = taskService;
        this.resourceLocator = resourceLocator;
    }

    @PostMapping
    public ResponseEntity<URI> createTask(@RequestBody final TaskDto taskDto) {
        Integer id = taskService.createTask(taskDto);
        var resourceUri = resourceLocator.getResourceUri(this.getClass(), "getTask", id);
        return ResponseEntity.created(resourceUri).body(resourceUri);
    }

    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable("id") final Integer id) {
        return taskService.getTaskById(id);
    }

    @PatchMapping(path = "/{id}")
    public TaskDto updateStatus(@PathVariable("id") final Integer id, @RequestBody TaskPatchDto patch) {
        return taskService.updateTaskPartially(id, patch);
    }

    @GetMapping("/person/{id}")
    public List<TaskDto> getTaskByPersonId(@PathVariable("id") final Integer id, @RequestParam(required = false) boolean onlyDone) {
        return taskService.getTasksByPerson(id, onlyDone);
    }

}
