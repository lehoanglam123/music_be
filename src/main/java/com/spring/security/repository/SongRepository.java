package com.spring.security.repository;

import com.spring.security.entity.SongEntity;
import com.spring.security.entity.SongSearchEntity;
import com.spring.security.model.Lyrics;
import com.spring.security.model.Song;
import com.spring.security.request.ListSongId;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SongRepository {
    List<SongSearchEntity> getAll();

    /**
     * @param entity
     * @return
     */
    Integer insertSong(SongEntity entity);

    Optional<Song> getSongById(int id);

    Optional<Song> getSongByName(String songName);

    void updateSongById(Song song);

    List<Integer> getListSongByList(ListSongId listId);

    void deleteSongByListId(ListSongId listId);

    List<SongSearchEntity> getListSongByUserId(int id);

    List<SongSearchEntity> searchSongByName(@Param("songName")String songName);

    void insertLyricsBatch(@Param("songId")Integer songId, @Param("lyricsList") List<Lyrics> listLyrics);
}
