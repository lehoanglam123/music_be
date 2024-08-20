package com.spring.security.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class SongEntity {
    private Integer id;
    private String songName;
    private Integer artistId;
    private Integer albumId;
    private Integer genreId;
    private Date releaseYear;
    private String audio;
    private String duration;

    public SongEntity() {

    }
}
