package com.spring.security.service.impl;

import com.spring.security.controller.AlbumController;
import com.spring.security.entity.AlbumEntity;
import com.spring.security.model.Album;
import com.spring.security.repository.AlbumRepository;
import com.spring.security.request.ListAlbumId;
import com.spring.security.response.album.CreateAlbumResponse;
import com.spring.security.response.album.DeleteAlbumResponse;
import com.spring.security.response.album.ListAlbumResponse;
import com.spring.security.response.album.UpdateAlbumResponse;
import com.spring.security.service.ServiceAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceAlbumImpl implements ServiceAlbum {

    @Autowired
    AlbumRepository albumRepository;

    @Override
    public ResponseEntity<ListAlbumResponse> getAll() {
        List<Album> listAlbum = albumRepository.getAll();
        if(listAlbum.isEmpty()) {
            ListAlbumResponse response = ListAlbumResponse.builder()
                    .status("204")
                    .message("No data!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        ListAlbumResponse response = ListAlbumResponse.builder()
                .status("200")
                .message("Get data successfully!")
                .data(listAlbum)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CreateAlbumResponse> createAlbum(int artistId, AlbumEntity entity) {
        Optional<Album> album = albumRepository.getAlbumByArtistIdAndName(artistId, entity.getAlbumName());
        if (album.isPresent()) {
            CreateAlbumResponse response = CreateAlbumResponse.builder()
                    .status("400")
                    .message("The album already exists!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<Album> albumByName = albumRepository.getAlbumByName(entity.getAlbumName());
        if (albumByName.isPresent()) {
            CreateAlbumResponse response = CreateAlbumResponse.builder()
                    .status("400")
                    .message("The album already exists!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        entity.setArtistId(artistId);
        int id = albumRepository.insertAlbum(entity);
        System.out.println(id);
        CreateAlbumResponse response = CreateAlbumResponse.builder()
                .status("200")
                .message("The album created successfully!")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UpdateAlbumResponse> updateAlbum(int id, AlbumEntity entity) {
        Optional<Album> albumById = albumRepository.getAlbumById(id);
        if(albumById.isPresent()){
            if(entity.getAlbumName().isEmpty()){
                UpdateAlbumResponse response = UpdateAlbumResponse.builder()
                        .status("400")
                        .message("Album name is cannot empty!")
                        .build();
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            albumById.get().setId(id);
            albumById.get().setAlbumName(entity.getAlbumName());
            albumById.get().setImageUrl(entity.getImageUrl());
            albumRepository.updateAlbum(albumById.get());
            UpdateAlbumResponse response = UpdateAlbumResponse.builder()
                    .status("200")
                    .message("Album updated successfully!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        UpdateAlbumResponse response = UpdateAlbumResponse.builder()
                .status("400")
                .message("Album doesn't exists!")
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<DeleteAlbumResponse> deleteListAlbum(ListAlbumId listId) {
        List<Album> listAlbum = albumRepository.getAlbumByListId(listId);
        if(listAlbum.size()==listId.getListAlbumId().size()){
            albumRepository.deleteListAlbum(listId);
            DeleteAlbumResponse response = DeleteAlbumResponse.builder()
                    .status("200")
                    .message("Successfully delete the list of albums!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        DeleteAlbumResponse response = DeleteAlbumResponse.builder()
                .status("400")
                .message("Failed to delete the list of albums!")
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
