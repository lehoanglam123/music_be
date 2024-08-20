package com.spring.security.service;


import com.spring.security.entity.AuthenticationRequest;
import com.spring.security.entity.RegisterRequest;
import com.spring.security.response.RegisterResponse;
import com.spring.security.response.authentication.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ServiceAuthentication {

    ResponseEntity<LoginResponse> checkAccountByUsernameAndPassword(AuthenticationRequest request);

    ResponseEntity<RegisterResponse> registerAccount(RegisterRequest request);
}
