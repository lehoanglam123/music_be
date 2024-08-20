package com.spring.security.response.genre;

import com.spring.security.model.Genre;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListGenreResponse {
    private String status;
    private String message;

    private List<Genre> data;
}
