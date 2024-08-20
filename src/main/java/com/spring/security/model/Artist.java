package com.spring.security.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Artist {
	private Integer id;
	private String artistName;
	private String biography;
	private String imageUrl;
}
