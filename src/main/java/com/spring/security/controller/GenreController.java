package com.spring.security.controller;


import com.spring.security.entity.GenreEntity;
import com.spring.security.request.ListGenreId;
import com.spring.security.response.genre.CreateGenreResponse;
import com.spring.security.response.genre.DeleteGenreResponse;
import com.spring.security.response.genre.ListGenreResponse;
import com.spring.security.response.genre.UpdateGenreResponse;
import com.spring.security.service.ServiceGenre;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/genre")
@ResponseBody
//@CrossOrigin(origins = "http://localhost:3000")
public class GenreController {
    @Autowired
    ServiceGenre serviceGenre;

    @GetMapping("/getAll")
    public ResponseEntity<ListGenreResponse> getAll(){
        return serviceGenre.getAll();
    }

    @PostMapping("/create")
    public ResponseEntity<CreateGenreResponse> insertGenre(@Valid @RequestBody GenreEntity entity){
        return serviceGenre.insertGenre(entity);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateGenreResponse> updateGenre(
            @PathVariable int id,
            @Valid @RequestBody GenreEntity entity){
        return serviceGenre.updateGenreById(id, entity);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteGenreResponse> deleteGenre(@RequestBody ListGenreId listId){
        return serviceGenre.deleteGenreById(listId);
    }
}
