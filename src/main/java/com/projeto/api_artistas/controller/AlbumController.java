package com.projeto.api_artistas.controller;

import com.projeto.api_artistas.dto.AlbumDTO;
import com.projeto.api_artistas.dto.ArtistaDTO;
import com.projeto.api_artistas.model.Album;
import com.projeto.api_artistas.service.AlbumService;
import com.projeto.api_artistas.service.S3Service;
import com.projeto.api_artistas.repository.AlbumRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/albuns") // Atende Requisito 'j' (Versionamento)
public class AlbumController {

    private final AlbumService albumService;
    private final S3Service s3Service;
    private final AlbumRepository albumRepository;

    public AlbumController(AlbumService albumService, S3Service s3Service, AlbumRepository albumRepository) {
        this.albumService = albumService;
        this.s3Service = s3Service;
        this.albumRepository = albumRepository;
    }

    /* GET com paginação e filtros de artista/tipo
     * Também gera link pré-assinado para acessar a imagem no MinIO
     */ 
    @GetMapping
    public Page<AlbumDTO> listar(
            @RequestParam(required = false) String nomeArtista,
            @RequestParam(required = false) String tipo,
            Pageable pageable) {
        
        return albumService.buscarAlbuns(nomeArtista, tipo, pageable).map(album -> {
            String urlAssinada = album.getImagemCapaUrl() != null ? 
                                 s3Service.gerarLinkPreAssinado(album.getImagemCapaUrl()) : null;
            
            return new AlbumDTO(
                album.getId(),
                album.getTitulo(),
                urlAssinada, 
                album.getArtistas().stream()
                        .map(artista -> new ArtistaDTO(artista.getId(), artista.getNome(), artista.getTipo()))
                        .collect(Collectors.toSet())
            );
        });
    }

    // POST para criação de novo álbum
    @PostMapping
    public ResponseEntity<Album> criar(@RequestBody Album album) {
        Album salvo = albumService.salvar(album);
        return ResponseEntity.ok(salvo);
    }

    /* PUT para atualização de álbum existente
     * Garante integridade do ID vindo da URL
     */
    @PutMapping("/{id}")
    public ResponseEntity<Album> atualizar(@PathVariable Long id, @RequestBody Album album) {
        return albumRepository.findById(id)
                .map(existente -> {
                    album.setId(id); // Garante a integridade do ID da URL
                    return ResponseEntity.ok(albumService.salvar(album));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /* Upload de múltiplas imagens de capa para um álbum
     * Salva o primeiro arquivo como imagem principal 
     */
    @PostMapping("/{id}/capas")
    public ResponseEntity<List<String>> uploadCapas(@PathVariable Long id, @RequestParam("files") MultipartFile[] files) {
        try {
            Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));
            
            List<String> fileNames = new ArrayList<>();
            for (MultipartFile file : files) {
                String fileName = s3Service.uploadFile(file);
                fileNames.add(fileName);
            }

            if (!fileNames.isEmpty()) {
                album.setImagemCapaUrl(fileNames.get(0)); 
                albumRepository.save(album);
            }
        
            return ResponseEntity.ok(fileNames);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}