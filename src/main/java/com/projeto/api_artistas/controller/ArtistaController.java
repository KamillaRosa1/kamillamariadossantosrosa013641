package com.projeto.api_artistas.controller;

import com.projeto.api_artistas.dto.ArtistaDTO;
import com.projeto.api_artistas.model.Artista;
import com.projeto.api_artistas.service.ArtistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// Controller responsável por operações de CRUD de artistas
@RestController
@RequestMapping("/api/v1/artistas") 
public class ArtistaController {

    private final ArtistaService artistaService;

    public ArtistaController(ArtistaService artistaService) {
        this.artistaService = artistaService;
    }

    @GetMapping
    public List<ArtistaDTO> listar() {
        return artistaService.listarTodos().stream()
                .map(artista -> new ArtistaDTO(
                    artista.getId(), 
                    artista.getNome(), 
                    artista.getTipo()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Artista> criar(@RequestBody Artista artista) {
        return ResponseEntity.ok(artistaService.salvar(artista));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artista> atualizar(@PathVariable Long id, @RequestBody Artista artista) {
        // Verifica se o artista existe antes de atualizar
        return artistaService.buscarPorId(id) 
                .map(existente -> {
                    artista.setId(id); // garante que o ID não seja sobrescrito
                    return ResponseEntity.ok(artistaService.salvar(artista));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}