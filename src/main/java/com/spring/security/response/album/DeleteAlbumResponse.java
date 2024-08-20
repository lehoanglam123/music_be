package com.spring.security.response.album;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteAlbumResponse {
    private String status;
    private String message;
}
