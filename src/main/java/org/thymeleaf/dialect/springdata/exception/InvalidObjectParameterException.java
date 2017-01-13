package org.thymeleaf.dialect.springdata.exception;

public class InvalidObjectParameterException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidObjectParameterException(String msg) {
        super(msg);
    }

}
