package com.spring.security.repository;

import com.spring.security.entity.AlbumEntity;
import com.spring.security.model.Album;
import com.spring.security.request.ListAlbumId;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AlbumRepository {
    List<Album> getAll();

    Optional<Album> getAlbumByArtistIdAndName(int artistId, String albumName);

    int insertAlbum(AlbumEntity entity);

    Optional<Album> getAlbumById(int id);

    Optional<Album> getAlbumByName(String albumName);

    boolean updateAlbum(Album album);

    List<Album> getAlbumByListId(ListAlbumId listId);

    boolean deleteListAlbum(ListAlbumId listId);
}
