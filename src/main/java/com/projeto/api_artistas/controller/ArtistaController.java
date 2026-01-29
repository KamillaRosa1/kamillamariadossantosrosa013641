package com.projeto.api_artistas.controller;

import com.projeto.api_artistas.model.Artista;
import com.projeto.api_artistas.service.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/artistas")
@Tag(name = "Artistas", description = "Endpoints para gerenciamento de artistas")
public class ArtistaController {

    private final ArtistaService artistaService;

    public ArtistaController(ArtistaService artistaService) {
        this.artistaService = artistaService;
    }

    // Paginação, Filtros e Ordenação
    @GetMapping
    @Operation(summary = "Lista artistas com paginação, filtros (nome/tipo) e ordenação")
    public ResponseEntity<Page<Artista>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String tipo,
            Pageable pageable) {
        return ResponseEntity.ok(artistaService.listarComFiltros(nome, tipo, pageable));
    }

    // POST
    @PostMapping
    @Operation(summary = "Cria um novo artista")
    public ResponseEntity<Artista> criar(@RequestBody Artista artista) {
        return ResponseEntity.ok(artistaService.salvar(artista));
    }

    // PUT
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um artista existente")
    public ResponseEntity<Artista> atualizar(@PathVariable Long id, @RequestBody Artista artista) {
        return artistaService.buscarPorId(id)
                .map(existente -> {
                    artista.setId(id);
                    return ResponseEntity.ok(artistaService.salvar(artista));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}