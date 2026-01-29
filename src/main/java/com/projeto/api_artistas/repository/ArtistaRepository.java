package com.projeto.api_artistas.repository;

import com.projeto.api_artistas.model.Artista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    // Atende Requisito 'f': Busca por nome com paginação/ordenação
    Page<Artista> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    // Atende Requisito 'e': Busca por tipo
    Page<Artista> findByTipoIgnoreCase(String tipo, Pageable pageable);
}