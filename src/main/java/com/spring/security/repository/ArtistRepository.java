package com.spring.security.repository;

import java.util.List;
import java.util.Optional;

import com.spring.security.entity.ArtistEntity;
import com.spring.security.request.ListArtistId;
import org.apache.ibatis.annotations.Mapper;

import com.spring.security.model.Artist;

@Mapper
public interface ArtistRepository {

	List<Artist> getAll();

    Optional<Artist> getArtistByName(String artistName);

    boolean insertArtist(ArtistEntity entity);

    Optional<Artist> getArtistById(int id);

    boolean updateArtistById(Artist artist);

    List<Artist> getArtistByListId(ListArtistId listId);

    boolean deleteArtistByListId(ListArtistId listId);
}
