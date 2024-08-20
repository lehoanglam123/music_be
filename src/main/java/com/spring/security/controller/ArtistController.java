package com.spring.security.controller;

import com.spring.security.entity.ArtistEntity;
import com.spring.security.request.ListArtistId;
import com.spring.security.response.artist.CreateArtistResponse;
import com.spring.security.response.artist.DeleteArtistResponse;
import com.spring.security.response.artist.ListArtistResponse;
import com.spring.security.response.artist.UpdateArtistResponse;
import com.spring.security.service.ServiceArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/artist")
@ResponseBody
@CrossOrigin(origins = "http://localhost:3000")
public class ArtistController {
	
	@Autowired
	private ServiceArtist serviceArtist;

	@GetMapping("/getAll")
	public ResponseEntity<ListArtistResponse> getAll(){
		return serviceArtist.getAll();
	}

	@PostMapping("/create")
	public ResponseEntity<CreateArtistResponse> createArtist(@RequestBody ArtistEntity entity){
		return serviceArtist.createArtist(entity);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<UpdateArtistResponse> updateArtist(
			@PathVariable int id,
			@RequestBody ArtistEntity entity){
		return serviceArtist.updateArtist(id, entity);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<DeleteArtistResponse> deleteArtist(@RequestBody ListArtistId listId){
		return serviceArtist.deleteArtist(listId);
	}

}
