package com.spring.security.service;

import com.spring.security.entity.ArtistEntity;
import com.spring.security.request.ListArtistId;
import com.spring.security.response.artist.CreateArtistResponse;
import com.spring.security.response.artist.DeleteArtistResponse;
import com.spring.security.response.artist.UpdateArtistResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.security.response.artist.ListArtistResponse;

@Service
public interface ServiceArtist {

	ResponseEntity<ListArtistResponse> getAll();

    ResponseEntity<CreateArtistResponse> createArtist(ArtistEntity entity);

    ResponseEntity<UpdateArtistResponse> updateArtist(int id, ArtistEntity entity);

    ResponseEntity<DeleteArtistResponse> deleteArtist(ListArtistId listId);
}
