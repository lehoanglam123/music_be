package com.spring.security.entity;

import com.spring.security.model.Lyrics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SongSearchEntity {
    private Integer id;
    private String songName;
    private String artistName;
    private Integer duration;
    private String audio;
    private List<Lyrics> lyrics;

    public SongSearchEntity() {

    }
}
