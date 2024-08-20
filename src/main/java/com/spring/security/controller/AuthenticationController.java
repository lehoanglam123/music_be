package com.spring.security.controller;

import com.spring.security.entity.AuthenticationRequest;
import com.spring.security.entity.RegisterRequest;
import com.spring.security.response.RegisterResponse;
import com.spring.security.response.authentication.LoginResponse;
import com.spring.security.service.ServiceAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@ResponseBody
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    @Autowired
    ServiceAuthentication serviceAuthentication;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody AuthenticationRequest request){
        return serviceAuthentication.checkAccountByUsernameAndPassword(request);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request){
        return serviceAuthentication.registerAccount(request);
    }
}
