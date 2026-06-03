package org.example.springsecuritycustomauthn.service;

public interface AuthenticationService {

    void login(String username, String password);
}