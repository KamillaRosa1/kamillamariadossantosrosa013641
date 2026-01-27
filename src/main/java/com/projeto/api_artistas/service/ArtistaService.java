package com.projeto.api_artistas.service;

import com.projeto.api_artistas.model.Artista;
import com.projeto.api_artistas.repository.ArtistaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistaService {

    private final ArtistaRepository artistaRepository;

    public ArtistaService(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    public List<Artista> listarTodos() {
        return artistaRepository.findAll();
    }

    public Artista salvar(Artista artista) {
        return artistaRepository.save(artista);
    }

    public Optional<Artista> buscarPorId(Long id) {
        return artistaRepository.findById(id);
    }
}

