package com.spring.security.response.song;

import com.spring.security.entity.SongSearchEntity;
import com.spring.security.model.Song;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListSongResponse {
    private String status;
    private String message;

    private List<SongSearchEntity> data;
}
