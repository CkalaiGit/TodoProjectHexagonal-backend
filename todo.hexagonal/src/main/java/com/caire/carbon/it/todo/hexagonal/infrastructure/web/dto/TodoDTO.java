package com.caire.carbon.it.todo.hexagonal.infrastructure.web.dto;

import com.caire.carbon.it.todo.hexagonal.domain.model.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Getter
@Setter
@AllArgsConstructor
public class TodoDTO {
    private long id;
    private String title;
    private boolean completed;
    private URI url;

    public static TodoDTO toDTO(Todo todo) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(todo.id())
                .toUri();
        return new TodoDTO(todo.id(), todo.title(), todo.completed(), location);
    }

}
