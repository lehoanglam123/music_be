package com.spring.security.service.impl;

import java.util.List;
import java.util.Optional;

import com.spring.security.entity.ArtistEntity;
import com.spring.security.request.ListArtistId;
import com.spring.security.response.artist.CreateArtistResponse;
import com.spring.security.response.artist.DeleteArtistResponse;
import com.spring.security.response.artist.UpdateArtistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.security.model.Artist;
import com.spring.security.repository.ArtistRepository;
import com.spring.security.response.artist.ListArtistResponse;
import com.spring.security.service.ServiceArtist;

@Service
public class ServiceArtistImpl  implements ServiceArtist{
	
	@Autowired
	private ArtistRepository artistRepository;
	@Override
	public ResponseEntity<ListArtistResponse> getAll() {
		List<Artist> listArtist = artistRepository.getAll();
		if(listArtist.isEmpty()) {
			ListArtistResponse response = ListArtistResponse.builder()
					.status("204")
					.message("No data!")
					.build();
			return new ResponseEntity<ListArtistResponse>(response, HttpStatus.NO_CONTENT);
		}
		ListArtistResponse response = ListArtistResponse.builder()
				.status("200")
				.message("Get data successfully!")
				.data(listArtist)
				.build();
		return new ResponseEntity<ListArtistResponse>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CreateArtistResponse> createArtist(ArtistEntity entity) {
		Optional<Artist> artist = artistRepository.getArtistByName(entity.getArtistName());
		if(artist.isEmpty()) {
			artistRepository.insertArtist(entity);
			CreateArtistResponse response = CreateArtistResponse.builder()
					.status("200")
					.message("Successfully create a artist!")
					.build();
			return new ResponseEntity<CreateArtistResponse>(response, HttpStatus.OK);
		}
		CreateArtistResponse response = CreateArtistResponse.builder()
				.status("400")
				.message("The artist name already exists!")
				.build();
		return new ResponseEntity<CreateArtistResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<UpdateArtistResponse> updateArtist(int id, ArtistEntity entity) {
		Optional<Artist> artist = artistRepository.getArtistById(id);
		if(artist.isPresent()) {
			Optional<Artist> artistByName = artistRepository.getArtistByName(entity.getArtistName());
			if(artistByName.isEmpty()) {
				artist.get().setArtistName(entity.getArtistName());
				artist.get().setBiography(entity.getBiography());
				artist.get().setImageUrl(entity.getImageUrl());
				artistRepository.updateArtistById(artist.get());
				UpdateArtistResponse response = UpdateArtistResponse.builder()
						.status("200")
						.message("Update successfully!")
						.build();
				return new ResponseEntity<UpdateArtistResponse>(response, HttpStatus.OK);
			}
			UpdateArtistResponse response = UpdateArtistResponse.builder()
					.status("400")
					.message("Artist name is already exists!")
					.build();
			return new ResponseEntity<UpdateArtistResponse>(response, HttpStatus.OK);
		}
		UpdateArtistResponse response = UpdateArtistResponse.builder()
				.status("200")
				.message("Update successfully!")
				.build();
		return new ResponseEntity<UpdateArtistResponse>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DeleteArtistResponse> deleteArtist(ListArtistId listId) {
		List<Artist> listArtist = artistRepository.getArtistByListId(listId);
		if(listArtist.size()==listId.getListId().size()) {
			artistRepository.deleteArtistByListId(listId);
			DeleteArtistResponse response = DeleteArtistResponse.builder()
					.status("200")
					.message("Successfully delete the list of songs")
					.build();
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		artistRepository.deleteArtistByListId(listId);
		DeleteArtistResponse response = DeleteArtistResponse.builder()
				.status("400")
				.message("Failed to delete the list of songs")
				.build();
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}