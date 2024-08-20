package com.spring.security.service;

import com.spring.security.entity.AlbumEntity;
import com.spring.security.request.ListAlbumId;
import com.spring.security.response.album.CreateAlbumResponse;
import com.spring.security.response.album.DeleteAlbumResponse;
import com.spring.security.response.album.ListAlbumResponse;
import com.spring.security.response.album.UpdateAlbumResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ServiceAlbum {
    ResponseEntity<ListAlbumResponse> getAll();

    ResponseEntity<CreateAlbumResponse> createAlbum(int artistId, AlbumEntity entity);


    ResponseEntity<UpdateAlbumResponse> updateAlbum(int id, AlbumEntity entity);

    ResponseEntity<DeleteAlbumResponse> deleteListAlbum(ListAlbumId listId);
}
