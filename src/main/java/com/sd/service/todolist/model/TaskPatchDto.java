package com.sd.service.todolist.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sd.service.todolist.entity.TaskStatus;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record TaskPatchDto(String description, TaskStatus status) {}
