package com.spring.security.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AlbumEntity {
    private Integer artistId;
    private String albumName;
    private Date releaseYear;
    private String imageUrl;
}
