package com.spring.security.response.song;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteSongResponse {
    private String status;
    private String message;
}
