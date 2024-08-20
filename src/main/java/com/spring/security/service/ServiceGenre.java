package com.spring.security.service;

import com.spring.security.entity.GenreEntity;
import com.spring.security.request.ListGenreId;
import com.spring.security.response.genre.CreateGenreResponse;
import com.spring.security.response.genre.DeleteGenreResponse;
import com.spring.security.response.genre.ListGenreResponse;
import com.spring.security.response.genre.UpdateGenreResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ServiceGenre {
    ResponseEntity<ListGenreResponse> getAll();

    ResponseEntity<CreateGenreResponse> insertGenre(GenreEntity entity);

    ResponseEntity<UpdateGenreResponse> updateGenreById(int id, GenreEntity entity);

    ResponseEntity<DeleteGenreResponse> deleteGenreById(ListGenreId listId);
}
