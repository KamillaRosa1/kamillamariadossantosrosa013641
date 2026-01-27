package com.projeto.api_artistas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.projeto.api_artistas.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    // Para busca por nome do artista
    Page<Album> findByArtistas_NomeContainingIgnoreCase(String nomeArtista, Pageable pageable);

    // Para busca parametrizada por tipo (CANTOR ou BANDA)
    Page<Album> findByArtistas_Tipo(String tipo, Pageable pageable);
}