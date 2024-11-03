package com.caire.carbon.it.todo.hexagonal.infrastructure.persistence;

import com.caire.carbon.it.todo.hexagonal.domain.model.Todo;

import java.util.List;
import java.util.stream.Collectors;

public class TodoConverter {

    public static TodoJpaEntity toJpaEntity(Todo todo) {
        return new TodoJpaEntity(todo.order(), todo.title(), todo.completed());
    }

    public static Todo toDomain(TodoJpaEntity todoJpaEntity) {
        return new Todo(todoJpaEntity.getId(), todoJpaEntity.getTitle(), todoJpaEntity.isComplete());
    }

    public static List<Todo> toDomainList(List<TodoJpaEntity> todoJpaEntities) {
        return todoJpaEntities.stream()
                .map(TodoConverter::toDomain)
                .collect(Collectors.toList());
    }
}