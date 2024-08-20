package com.spring.security.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Song {
    private Integer id;
    private String songName;
    private Integer artistId;
    private Integer albumId;
    private Integer genreId;
    private Date releaseYear;
    private String audio;
    private String duration;
    private Boolean status;
    private Integer accountId;
}
