package org.thymeleaf.dialect.springdata.exception;

public class PaginationDecoratorNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PaginationDecoratorNotFoundException(String message) {
        super(message);
    }

}
