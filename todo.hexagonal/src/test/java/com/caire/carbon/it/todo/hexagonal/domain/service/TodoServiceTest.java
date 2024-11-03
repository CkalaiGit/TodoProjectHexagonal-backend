package com.caire.carbon.it.todo.hexagonal.domain.service;

import com.caire.carbon.it.todo.hexagonal.domain.exception.DuplicateTitleException;
import com.caire.carbon.it.todo.hexagonal.domain.model.Todo;
import com.caire.carbon.it.todo.hexagonal.domain.repository.ITodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private ITodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    public void testCreateTodo_Success() {
        Todo todo = new Todo(1L, "New Task", false);
        when(todoRepository.existsByTitle(todo.title())).thenReturn(false);
        when(todoRepository.save(todo)).thenReturn(todo);
        Todo createdTodo = todoService.createTodo(todo);
        assertNotNull(createdTodo);
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    public void testCreateTodo_DuplicateTitleException() {
        Todo todo = new Todo(1L, "New Task", false);
        when(todoRepository.existsByTitle(todo.title())).thenReturn(true);
        assertThrows(DuplicateTitleException.class, () -> {
            todoService.createTodo(todo);
        });
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    public void testGetAllTodos() {
        List<Todo> todos = Arrays.asList(new Todo(1L, "New Task", false),
                                         new Todo(2L, "New Task 2", true));
        when(todoRepository.findAll()).thenReturn(todos);
        List<Todo> result = todoService.getAllTodos();
        assertEquals(2, result.size());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Todo todo = new Todo(1L, "New Task", false);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        Optional<Todo> result = todoService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(todo, result.get());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteTodoById() {
        doNothing().when(todoRepository).deleteById(1L);
        todoService.deleteTodoById(1L);
        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTodos() {
        doNothing().when(todoRepository).deleteAllTodos();
        todoService.deleteTodos();
        verify(todoRepository, times(1)).deleteAllTodos();
    }
}
