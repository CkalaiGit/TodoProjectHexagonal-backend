package com.caire.carbon.it.todo.hexagonal.domain.exception;

public class DuplicateTitleException extends RuntimeException {

    public DuplicateTitleException(String message) {
        super(message);
    }
}
