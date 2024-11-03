package com.caire.carbon.it.todo.hexagonal.domain.repository;

import com.caire.carbon.it.todo.hexagonal.domain.model.Todo;

import java.util.List;
import java.util.Optional;
public interface ITodoRepository  {

    Todo save(Todo todo);
    Optional<Todo> findById(long id);
    List<Todo> findAll();
    void deleteById(long id);
    boolean existsByTitle(String title);
    void deleteAllTodos();
}
