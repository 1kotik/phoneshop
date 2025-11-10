package com.es.core.service;

import com.es.core.model.AuthenticationRequest;

public interface AuthenticationService {
    void login(AuthenticationRequest authenticationRequest);
    void logout();
}
