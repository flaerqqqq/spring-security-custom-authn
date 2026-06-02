package org.example.springsecuritycustomauthn.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
        super("Role not found");
    }

    public RoleNotFoundException(String msg) {
        super(msg);
    }

    public RoleNotFoundException(String fieldName, Object value) {
        super(String.format("Role not found with %s: %s", fieldName, value.toString()));
    }
}