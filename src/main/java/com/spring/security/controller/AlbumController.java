package com.spring.security.controller;

import com.spring.security.entity.AlbumEntity;
import com.spring.security.request.ListAlbumId;
import com.spring.security.response.album.CreateAlbumResponse;
import com.spring.security.response.album.DeleteAlbumResponse;
import com.spring.security.response.album.ListAlbumResponse;
import com.spring.security.response.album.UpdateAlbumResponse;
import com.spring.security.service.ServiceAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/album")
@ResponseBody
@CrossOrigin(origins = "http://localhost:3000")
public class AlbumController {

    @Autowired
    private ServiceAlbum serviceAlbum;

    @GetMapping("/getAll")
    public ResponseEntity<ListAlbumResponse> getAll(){
        return serviceAlbum.getAll();
    }

    @PostMapping("/create/{artistId}")
    public ResponseEntity<CreateAlbumResponse> createAlbum(
           @PathVariable int artistId,
           @RequestBody AlbumEntity entity){
        return serviceAlbum.createAlbum(artistId, entity);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateAlbumResponse> updateAlum(
            @PathVariable int id,
            @RequestBody AlbumEntity entity){
        return serviceAlbum.updateAlbum(id, entity);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteAlbumResponse> deleteListAlbum(@RequestBody ListAlbumId listId) {
        return serviceAlbum.deleteListAlbum(listId);
    }
}
