package com.caire.carbon.it.todo.hexagonal.infrastructure.persistence;

import com.caire.carbon.it.todo.hexagonal.domain.repository.ITodoRepository;
import com.caire.carbon.it.todo.hexagonal.domain.model.Todo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Getter
@Setter
public class TodoRepository implements ITodoRepository {

    private final ITodoJpaRepository jpaRepository;

    public TodoRepository(ITodoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Todo save(Todo todo) {
        TodoJpaEntity jpaEntity = TodoConverter.toJpaEntity(todo);
        return TodoConverter.toDomain(jpaRepository.save(jpaEntity));
    }

    @Override
    public Optional<Todo> findById(long id) {
        return jpaRepository.findById(id)
                .map(TodoConverter::toDomain);
    }

    @Override
    public List<Todo> findAll() {
        return TodoConverter.toDomainList(jpaRepository.findAll());
    }

    @Override
    public void deleteById(long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByTitle(String title) {
        return jpaRepository.existsByTitle(title);
    }

    @Override
    public void deleteAllTodos() {
        jpaRepository.deleteAll();
    }
}
