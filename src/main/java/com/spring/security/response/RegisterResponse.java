package com.spring.security.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private String status;
    private String message;
    private String token;
    private String refreshToken;
}
