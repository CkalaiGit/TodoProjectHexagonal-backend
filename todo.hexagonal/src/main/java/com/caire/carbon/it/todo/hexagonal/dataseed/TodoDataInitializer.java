package com.caire.carbon.it.todo.hexagonal.domain.dataseed;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
@Profile("!prod")
public class TodoDataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(TodoDataInitializer.class);

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator(
            @Value("classpath:data-seeds/work-todos.json") Resource workTodos) {

        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[]{workTodos});

        logger.info("Initializing todo database with JSON data");

        return factory;
    }
}
