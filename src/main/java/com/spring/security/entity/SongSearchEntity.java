package com.spring.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class SongSearchEntity {
    private Integer id;
    private String songName;
    private String genreName;
    private String artistName;
    private Integer albumId;
    private Date releaseYear;
    private String audio;
    private String duration;
    private Integer accountId;

    public SongSearchEntity() {

    }
}
