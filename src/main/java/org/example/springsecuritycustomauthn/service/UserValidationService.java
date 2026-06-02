package org.example.springsecuritycustomauthn.service;

public interface UserValidationService {

    void throwIfUsernameExists(String username);
}