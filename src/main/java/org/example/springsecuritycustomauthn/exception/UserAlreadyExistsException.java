package org.example.springsecuritycustomauthn.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User already exists");
    }

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }

    public UserAlreadyExistsException(String fieldName, Object value) {
        super(String.format("User already exists with %s: %s", fieldName, value.toString()));
    }
}