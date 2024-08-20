package com.spring.security.response.artist;

import java.util.List;

import com.spring.security.model.Artist;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ListArtistResponse {
	private String status;
	private String message;
	
	private List<Artist> data;
}
