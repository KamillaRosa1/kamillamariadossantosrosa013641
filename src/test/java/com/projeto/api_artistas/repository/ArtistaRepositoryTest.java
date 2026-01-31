package com.projeto.api_artistas.repository;

import com.projeto.api_artistas.model.Artista;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ArtistaRepositoryTest {

    @Autowired
    private ArtistaRepository repository;

    @Test
    @DisplayName("Deve filtrar artistas por nome e aplicar ordenação alfabética")
    void deveFiltrarPorNomeEOrdenar() {
        repository.save(new Artista(null, "Linkin Park", "Banda", null));
        repository.save(new Artista(null, "Lady Gaga", "Cantor", null));
        // Garante que o H2 persistiu antes da busca
        repository.flush();

        PageRequest pageRequest = PageRequest.of(0, 10, org.springframework.data.domain.Sort.by("nome").ascending());
        Page<Artista> resultado = repository.findByNomeContainingIgnoreCase("A", pageRequest);

        // Verificação:
        // Serj, Mike do migration V2 + Linkin, Lady do Teste
        assertThat(resultado.getContent()).hasSize(4);

        assertThat(resultado.getContent().get(0).getNome()).isEqualTo("Lady Gaga");
    }
}
