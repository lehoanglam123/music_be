package com.spring.security.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Album {
    private Integer id;
    private Integer artistId;
    private String albumName;
    private Date releaseYear;
    private String imageUrl;
}
