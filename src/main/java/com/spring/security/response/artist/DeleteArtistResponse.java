package com.spring.security.response.artist;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteArtistResponse {
    private String status;
    private String message;
}