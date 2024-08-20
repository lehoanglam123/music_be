package com.spring.security.response.genre;

import com.spring.security.model.Genre;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateGenreResponse {
    private String status;
    private String message;
}
