package com.caire.carbon.it.todo.hexagonal.infrastructure.config;

import com.caire.carbon.it.todo.hexagonal.infrastructure.persistence.TodoRepository;
import com.caire.carbon.it.todo.hexagonal.domain.service.TodoService;
import com.caire.carbon.it.todo.hexagonal.domain.service.ITodoService;
import com.caire.carbon.it.todo.hexagonal.infrastructure.persistence.ITodoJpaRepository;
import com.caire.carbon.it.todo.hexagonal.domain.repository.ITodoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.caire.carbon.it.todo.hexagonal")
public class AppConfig {

    @Bean(name = "customTodoRepository")
    public ITodoRepository todoRepository(ITodoJpaRepository jpaRepository) {
        return new TodoRepository(jpaRepository);
    }

    @Bean
    public ITodoService todoService(ITodoRepository todoRepository) {
        return new TodoService(todoRepository);
    }
}
