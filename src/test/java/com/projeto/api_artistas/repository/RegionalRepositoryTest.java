package com.projeto.api_artistas.repository;

import com.projeto.api_artistas.model.Regional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RegionalRepositoryTest {

    @Autowired
    private RegionalRepository repository;

    @Test
    @DisplayName("Deve buscar regional pelo ID externo corretamente")
    void deveBuscarPorIdExterno() {
        Regional regional = Regional.builder()
                .idExterno(123L)
                .nome("Sudeste")
                .ativo(true)
                .build();
        repository.save(regional);

        Optional<Regional> encontrado = repository.findByIdExterno(123L);

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Sudeste");
    }
}