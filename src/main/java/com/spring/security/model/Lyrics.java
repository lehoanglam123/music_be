package com.spring.security.model;

import lombok.Builder;
import lombok.Data;

@Data
public class Lyrics {
    private String timestamp;
    private String lyric;

    public Lyrics(){}

    public Lyrics (String timestamp, String lyric) {
        this.timestamp = timestamp;
        this.lyric = lyric;
    }
}
