package com.caire.carbon.it.todo.hexagonal.domain.service;

import com.caire.carbon.it.todo.hexagonal.domain.exception.DuplicateTitleException;
import com.caire.carbon.it.todo.hexagonal.domain.model.Todo;
import com.caire.carbon.it.todo.hexagonal.domain.repository.ITodoRepository;

import java.util.List;
import java.util.Optional;

public class TodoService implements ITodoService {

    private final ITodoRepository todoRepository;

    public TodoService(ITodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Todo createTodo(Todo todo) {
        if (existsByTitle(todo.title())) {
            throw new DuplicateTitleException("Un todo avec le titre '" + todo.title() + "' existe déjà.");
        }
        return  todoRepository.save(todo);
    }

    @Override
    public Todo update(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public Optional<Todo> findById(long id) {
        return todoRepository.findById(id);
    }

    @Override
    public void deleteTodoById(long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public void deleteTodos() {
        todoRepository.deleteAllTodos();
    }

    @Override
    public boolean existsByTitle(String title) {
        return todoRepository.existsByTitle(title);
    }

}
