package com.spring.security.repository;

import com.spring.security.entity.SongEntity;
import com.spring.security.entity.SongSearchEntity;
import com.spring.security.model.Song;
import com.spring.security.request.ListSongId;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SongRepository {
    List<SongSearchEntity> getAll();

    boolean insertSong(SongEntity entity);

    Optional<Song> getSongById(int id);

    Optional<Song> getSongByName(String songName);

    boolean updateSongById(Song song);

    List<Integer> getListSongByList(ListSongId listId);

    boolean deleteSongByListId(ListSongId listId);

    List<SongSearchEntity> getListSongByUserId(int id);

    List<SongSearchEntity> searchSongByName(String songName);
}
