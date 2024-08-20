package com.spring.security.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String username;
    private String password;
    private String gmail;
}
