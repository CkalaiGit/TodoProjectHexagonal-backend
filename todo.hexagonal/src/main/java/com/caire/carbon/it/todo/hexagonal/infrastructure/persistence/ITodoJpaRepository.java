package com.caire.carbon.it.todo.hexagonal.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITodoJpaRepository extends JpaRepository<TodoJpaEntity, Long> {

    boolean existsByTitle(String title);
}
