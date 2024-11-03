package com.caire.carbon.it.todo.hexagonal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class TodoApplicationHexagonal {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplicationHexagonal.class, args);
	}

}
