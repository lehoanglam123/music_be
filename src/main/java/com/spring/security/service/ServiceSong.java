package com.spring.security.service;

import com.spring.security.entity.SongEntity;
import com.spring.security.request.ListSongId;
import com.spring.security.response.song.CreateSongResponse;
import com.spring.security.response.song.DeleteSongResponse;
import com.spring.security.response.song.ListSongResponse;
import com.spring.security.response.song.UpdateSongResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ServiceSong {
    ResponseEntity<ListSongResponse> getAll();

    ResponseEntity<CreateSongResponse> insertSong(MultipartFile file, SongEntity entity) throws Exception;

    ResponseEntity<UpdateSongResponse> updateSong(int id, SongEntity entity) throws Exception;

    ResponseEntity<DeleteSongResponse> deleteSong(ListSongId listId) throws Exception;

    ResponseEntity<ListSongResponse> searchSongByName(String songName);

    ResponseEntity<ListSongResponse> getListSongByUserId(int id);
}
