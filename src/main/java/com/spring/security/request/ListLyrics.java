package com.spring.security.request;

import com.spring.security.model.Lyrics;

import java.util.List;

public class ListLyrics {
    private List<Lyrics> listLyrics;

    public List<Lyrics> getListLyrics() {
        return this.listLyrics;
    }

    public void setListLyrics(List<Lyrics> list) {
        this.listLyrics = list;
    }
}
