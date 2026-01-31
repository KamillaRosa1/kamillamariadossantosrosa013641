package com.projeto.api_artistas.service;

import com.projeto.api_artistas.dto.RegionalDTO;
import com.projeto.api_artistas.model.Regional;
import com.projeto.api_artistas.repository.RegionalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegionalServiceTest {

    @Mock
    private RegionalRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RegionalService service;

    @Test
    @DisplayName("Deve inserir nova regional ao sincronizar se o ID externo for inédito")
    void deveInserirNovaRegionalNaSincronizacao() {
        // Simula resposta da API externa
        RegionalDTO dto = new RegionalDTO(999L, "Centro-Oeste");
        RegionalDTO[] response = {dto};

        when(restTemplate.getForObject(anyString(), eq(RegionalDTO[].class))).thenReturn(response);
        when(repository.findAll()).thenReturn(List.of());
        when(repository.findByIdExterno(999L)).thenReturn(Optional.empty());

        service.sincronizar();

        // Verifica se persistiu a nova regional
        verify(repository, atLeastOnce()).save(any(Regional.class));
    }

    @Test
    @DisplayName("Deve inativar regional local se não estiver presente na resposta da API")
    void deveInativarRegionalAusenteNaAPI() {
        // API retorna lista vazia
        when(restTemplate.getForObject(anyString(), eq(RegionalDTO[].class))).thenReturn(new RegionalDTO[0]);
        
        // Banco tem uma regional ativa
        Regional regionalAtiva = Regional.builder()
                .id(1L).idExterno(50L).nome("Norte").ativo(true).build();
        when(repository.findAll()).thenReturn(List.of(regionalAtiva));

        service.sincronizar();

        // Verifica se mudou o estado para inativo e salvou
        verify(repository).save(argThat(r -> !r.isAtivo()));
    }
}