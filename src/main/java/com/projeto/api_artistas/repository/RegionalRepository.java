package com.projeto.api_artistas.repository;

import com.projeto.api_artistas.model.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RegionalRepository extends JpaRepository<Regional, Long> {
    // Para sincronizar, precisamos achar o registro por ID externo
    // independente de estar ativo ou não, para evitar duplicatas.
    Optional<Regional> findByIdExterno(Long idExterno);
}