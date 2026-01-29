package com.projeto.api_artistas.service;

import com.projeto.api_artistas.model.Artista;
import com.projeto.api_artistas.repository.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ArtistaService {

    private final ArtistaRepository artistaRepository;

    public ArtistaService(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    public Page<Artista> listarComFiltros(String nome, String tipo, Pageable pageable) {
        if (nome != null && !nome.isEmpty()) {
            return artistaRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        if (tipo != null && !tipo.isEmpty()) {
            return artistaRepository.findByTipoIgnoreCase(tipo, pageable);
        }
        return artistaRepository.findAll(pageable);
    }

    public Artista salvar(Artista artista) {
        return artistaRepository.save(artista);
    }

    public Optional<Artista> buscarPorId(Long id) {
        return artistaRepository.findById(id);
    }
}