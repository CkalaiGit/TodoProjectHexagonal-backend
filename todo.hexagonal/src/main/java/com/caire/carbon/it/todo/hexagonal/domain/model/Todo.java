package com.caire.carbon.it.todo.hexagonal.domain.model;

import jakarta.validation.constraints.NotBlank;

public record Todo(long id, @NotBlank(message = "Title cannot be null or empty") String title,
                   boolean completed) {}

