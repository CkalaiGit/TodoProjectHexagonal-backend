package com.caire.carbon.it.todo.hexagonal.domain.service;

import com.caire.carbon.it.todo.hexagonal.domain.model.Todo;

import java.util.List;
import java.util.Optional;

public interface ITodoService {

    Todo createTodo(Todo todo);
    Todo update(Todo todo);
    List<Todo> getAllTodos();
    Optional<Todo> findById(long id);
    void deleteTodoById(long id);
    void deleteTodos();
    boolean existsByTitle(String title);
}
