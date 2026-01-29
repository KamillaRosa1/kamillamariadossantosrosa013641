package com.projeto.api_artistas.controller;

import com.projeto.api_artistas.dto.AlbumDTO;
import com.projeto.api_artistas.model.Album;
import com.projeto.api_artistas.service.AlbumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/albuns")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public Page<Album> listar(
            @RequestParam(required = false) String artista,
            @RequestParam(required = false) String tipo,
            Pageable pageable) {
        return albumService.buscarAlbuns(artista, tipo, pageable);
    }

    @PostMapping
    public ResponseEntity<Album> criar(@RequestBody AlbumDTO dto) {
        return ResponseEntity.ok(albumService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> atualizar(@PathVariable Long id, @RequestBody AlbumDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(albumService.salvar(dto));
    }

    // Endpoint para Upload de Capa
    @PostMapping(value = "/{id}/capa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCapa(
            @PathVariable Long id, 
            @RequestParam("file") MultipartFile file) throws IOException {
        String urlPreAssinada = albumService.uploadCapa(id, file);
        return ResponseEntity.ok(urlPreAssinada);
    }
}