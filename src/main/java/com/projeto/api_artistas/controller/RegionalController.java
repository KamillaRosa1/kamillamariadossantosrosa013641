package com.projeto.api_artistas.controller;

import com.projeto.api_artistas.model.Regional;
import com.projeto.api_artistas.repository.RegionalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regionais")
@Tag(name = "Regionais", description = "Endpoints para consulta de regionais sincronizadas")
public class RegionalController {

    private final RegionalRepository repository;

    public RegionalController(RegionalRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Lista todas as regionais (Apenas ADMIN)")
    public ResponseEntity<List<Regional>> listarTodas() {
        return ResponseEntity.ok(repository.findAll());
    }
}