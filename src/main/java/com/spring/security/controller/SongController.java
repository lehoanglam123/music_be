package com.spring.security.controller;


import com.spring.security.entity.SongEntity;
import com.spring.security.request.ListSongId;
import com.spring.security.response.song.CreateSongResponse;
import com.spring.security.response.song.DeleteSongResponse;
import com.spring.security.response.song.ListSongResponse;
import com.spring.security.response.song.UpdateSongResponse;
import com.spring.security.service.ServiceSong;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/song")
@ResponseBody
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class SongController {
    @Autowired
    private ServiceSong serviceSong;

    @GetMapping("/getAll")
    public ResponseEntity<ListSongResponse> getAll() throws IOException {
        return serviceSong.getAll();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ListSongResponse> getSongByUserId(@PathVariable int id){
        return serviceSong.getListSongByUserId(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ListSongResponse> searchSong(
            @RequestParam(value = "q")
            @NotBlank(message = "q is required value") String songName
    ) {
        return serviceSong.searchSongByName(songName);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateSongResponse> createSong(@RequestPart("file") MultipartFile file,
                                                         @RequestPart("data") SongEntity entity) throws Exception{
        return serviceSong.uploadSong(file, entity);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateSongResponse> updateSong(@PathVariable int id,
                                                         @RequestBody SongEntity entity) throws Exception {
        return serviceSong.updateSong(id, entity);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteSongResponse> deleteSong(@RequestBody ListSongId listId) throws Exception {
        return serviceSong.deleteSong(listId);
    }
}
