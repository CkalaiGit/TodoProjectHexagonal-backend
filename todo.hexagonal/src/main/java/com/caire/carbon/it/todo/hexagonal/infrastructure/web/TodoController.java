package com.caire.carbon.it.todo.hexagonal.infrastructure.web;

import com.caire.carbon.it.todo.hexagonal.domain.model.Todo;
import com.caire.carbon.it.todo.hexagonal.domain.service.ITodoService;
import com.caire.carbon.it.todo.hexagonal.infrastructure.web.dto.TodoDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final ITodoService todoService;

    public TodoController(ITodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    @CrossOrigin(methods = RequestMethod.POST)
    public ResponseEntity<TodoDTO> createTodo(@RequestBody @Valid Todo todo) {
        if (todo.title() == null || todo.title().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        Todo createdTodo = todoService.createTodo(todo);
        TodoDTO todoDTO = TodoDTO.toDTO(createdTodo);
        return ResponseEntity.created(todoDTO.getUrl()).body(todoDTO);
    }


    @GetMapping()
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<List<TodoDTO>> getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        List<TodoDTO> todoDTOs = todos.stream()
                .map(TodoDTO::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(todoDTOs);
    }

    @GetMapping("/{id}")
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<TodoDTO> getTodoById(@PathVariable long id) {
        return todoService.findById(id)
                .map(todo -> ResponseEntity.ok(TodoDTO.toDTO(todo)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(methods = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTodoById(@PathVariable long id) {
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    @CrossOrigin(methods = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAllTodos() {
        todoService.deleteTodos();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @CrossOrigin(methods = RequestMethod.PUT)
    public ResponseEntity<?> updateTodo(@PathVariable long id, @RequestBody @Valid Todo updatedTodo) {
        if (updatedTodo.title() == null || updatedTodo.title().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Title cannot be empty");
        }

        Optional<Todo> existingTodo = todoService.findById(id);
        if (existingTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!existingTodo.get().title().equals(updatedTodo.title()) && todoService.existsByTitle(updatedTodo.title())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A todo with this title already exists");
        }

        Todo todoToUpdate = new Todo(id, updatedTodo.title(), updatedTodo.completed());
        Todo updated = todoService.update(todoToUpdate);
        return ResponseEntity.ok(TodoDTO.toDTO(updated));
    }

}
