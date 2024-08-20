package com.spring.security.response.album;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateAlbumResponse {
    private String status;
    private String message;

}
