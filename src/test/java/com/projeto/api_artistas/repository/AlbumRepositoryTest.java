package com.projeto.api_artistas.repository;

import com.projeto.api_artistas.model.Album;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository repository;

    @Test
    void deveListarAlbunsComPaginacaoEOrdenacaoAlfabetica() {
        Album a1 = new Album(); a1.setTitulo("B Album");
        Album a2 = new Album(); a2.setTitulo("A Album");
        repository.save(a1);
        repository.save(a2);

        // Ordenação Alfabética e Paginação
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("titulo").ascending());
        Page<Album> result = repository.findAll(pageRequest);

        // 13 do migration V2 + 2 do teste
        assertThat(result.getContent()).hasSize(15);
        
        // "A Album" será o primeiro da lista por ordem alfabética
        assertThat(result.getContent().get(0).getTitulo()).isEqualTo("A Album");
        assertThat(result.getContent().get(1).getTitulo()).isEqualTo("B Album");
    }
}