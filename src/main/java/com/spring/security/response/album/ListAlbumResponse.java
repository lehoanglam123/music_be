package com.spring.security.response.album;

import com.spring.security.model.Album;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListAlbumResponse {
    private String status;
    private String message;

    private List<Album> data;
}
