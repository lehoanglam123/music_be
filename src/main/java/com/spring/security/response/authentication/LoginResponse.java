package com.spring.security.response.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String status;
    private String message;
    private String token;
    private String refreshToken;
}
