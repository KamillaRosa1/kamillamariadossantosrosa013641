package com.projeto.api_artistas.service;

import com.projeto.api_artistas.model.Artista;
import com.projeto.api_artistas.repository.ArtistaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ArtistaServiceTest {

    @Mock
    private ArtistaRepository repository;

    @InjectMocks
    private ArtistaService service;

    @Test
    @DisplayName("Deve chamar repository corretamente ao listar com filtros")
    void deveChamarRepositoryComFiltros() {
        Page<Artista> page = new PageImpl<>(List.of(new Artista()));
        when(repository.findByTipoIgnoreCase(eq("Banda"), any(Pageable.class))).thenReturn(page);

        Page<Artista> result = service.listarComFiltros(null, "Banda", Pageable.unpaged());

        assertNotNull(result);
        verify(repository, times(1)).findByTipoIgnoreCase(eq("Banda"), any(Pageable.class));
    }
}