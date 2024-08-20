package com.spring.security.repository;

import com.spring.security.model.Genre;
import com.spring.security.request.ListGenreId;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GenreRepository {
    List<Genre> getAll();

    Optional<Genre> getGenreByName(String genreName);

    boolean updateGenreStatusById(Integer id);

    boolean insertGenre(String genreName);

    Optional<Genre> getGenreById(int id);

    boolean updateGenreNameById(Genre genre);

    List<Integer> getListGenreByListId(ListGenreId id);

    boolean deleteGenreByListId(ListGenreId listId);
}
