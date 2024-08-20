package com.spring.security.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AuthenticationRequest {

    @NotNull
    private String gmail;
    @NotNull
    private String password;
}
