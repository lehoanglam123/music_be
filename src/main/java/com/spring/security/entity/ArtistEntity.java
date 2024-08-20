package com.spring.security.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArtistEntity {
    private String artistName;
    private String biography;
    private String imageUrl;
}
