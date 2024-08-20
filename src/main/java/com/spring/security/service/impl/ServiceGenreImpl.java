package com.spring.security.service.impl;

import com.spring.security.entity.GenreEntity;
import com.spring.security.model.Genre;
import com.spring.security.repository.GenreRepository;
import com.spring.security.request.ListGenreId;
import com.spring.security.response.genre.CreateGenreResponse;
import com.spring.security.response.genre.DeleteGenreResponse;
import com.spring.security.response.genre.ListGenreResponse;
import com.spring.security.response.genre.UpdateGenreResponse;
import com.spring.security.service.ServiceGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceGenreImpl implements ServiceGenre {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public ResponseEntity<ListGenreResponse> getAll() {
        List<Genre> listGenre = genreRepository.getAll();
        if(listGenre.isEmpty()) {
            ListGenreResponse response = ListGenreResponse.builder()
                    .status("204")
                    .message("Data is null!")
                    .build();
            return new ResponseEntity<ListGenreResponse>(response, HttpStatus.NO_CONTENT);
        }
        ListGenreResponse response = ListGenreResponse.builder()
                .status("200")
                .message("Get all Genre!")
                .data(listGenre)
                .build();
        return new ResponseEntity<ListGenreResponse>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CreateGenreResponse> insertGenre(GenreEntity entity) {
        Optional<Genre> genre = genreRepository.getGenreByName(entity.getGenreName());
        if(genre.isPresent()) {
            if(genre.get().getStatus()==1) {
                CreateGenreResponse response = CreateGenreResponse.builder()
                        .status("400")
                        .message("The genre already exists!")
                        .build();
                return new ResponseEntity<CreateGenreResponse>(response, HttpStatus.BAD_REQUEST);
            }
            genreRepository.updateGenreStatusById(genre.get().getId());
            CreateGenreResponse response = CreateGenreResponse.builder()
                    .status("200")
                    .message("Successfully create a genre!")
                    .build();
            return new ResponseEntity<CreateGenreResponse>(response, HttpStatus.CREATED);
        }
        genreRepository.insertGenre(entity.getGenreName());
        CreateGenreResponse response = CreateGenreResponse.builder()
                .status("200")
                .message("Successfully create a genre!")
                .build();
        return new ResponseEntity<CreateGenreResponse>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UpdateGenreResponse> updateGenreById(int id, GenreEntity entity) {
        Optional<Genre> genreById = genreRepository.getGenreById(id);
        if(genreById.isPresent()) {
            Optional<Genre> genre = genreRepository.getGenreByName(entity.getGenreName());
            if(genre.isPresent()) {
                UpdateGenreResponse response = UpdateGenreResponse.builder()
                        .status("400")
                        .message("The genre name already exists!")
                        .build();
                return new ResponseEntity<UpdateGenreResponse>(response, HttpStatus.BAD_REQUEST);
            }
            genreById.get().setGenreName(entity.getGenreName());
            genreRepository.updateGenreNameById(genreById.get());
            UpdateGenreResponse response = UpdateGenreResponse.builder()
                    .status("200")
                    .message("Update genre successfully!")
                    .data(genreById.get())
                    .build();
            return new ResponseEntity<UpdateGenreResponse>(response, HttpStatus.OK);
        }
        UpdateGenreResponse response = UpdateGenreResponse.builder()
                .status("400")
                .message("genre does not exist")
                .build();
        return new ResponseEntity<UpdateGenreResponse>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<DeleteGenreResponse> deleteGenreById(ListGenreId listId) {
        List<Integer> listGenreId = genreRepository.getListGenreByListId(listId);
        int size = listId.getListGenreId().size();
        if(listGenreId.size()==listId.getListGenreId().size()) {
            genreRepository.deleteGenreByListId(listId);
            DeleteGenreResponse response = DeleteGenreResponse.builder()
                    .status("200")
                    .message("Successfully delete the genre list!")
                    .build();
            return new ResponseEntity<DeleteGenreResponse>(response, HttpStatus.OK);
        }
        DeleteGenreResponse response = DeleteGenreResponse.builder()
                .status("400")
                .message("Failed to delete the genre list!")
                .build();
        return new ResponseEntity<DeleteGenreResponse>(response, HttpStatus.OK);
    }
}
