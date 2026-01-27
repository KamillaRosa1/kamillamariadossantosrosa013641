package com.projeto.api_artistas.service;

import com.projeto.api_artistas.model.Album;
import com.projeto.api_artistas.repository.AlbumRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    // Busca álbuns com base nos parâmetros fornecidos.
    public Page<Album> buscarAlbuns(String nomeArtista, String tipo, Pageable pageable) {
        if (nomeArtista != null && !nomeArtista.isEmpty()) {
            return albumRepository.findByArtistas_NomeContainingIgnoreCase(nomeArtista, pageable);
        }
        if (tipo != null && !tipo.isEmpty()) {
            return albumRepository.findByArtistas_Tipo(tipo, pageable);
        }
        return albumRepository.findAll(pageable);
    }

    public Album salvar(Album album) {
        return albumRepository.save(album);
    }
}