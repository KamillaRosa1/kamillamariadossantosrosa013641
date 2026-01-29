package com.projeto.api_artistas.service;

import com.projeto.api_artistas.dto.RegionalDTO;
import com.projeto.api_artistas.model.Regional;
import com.projeto.api_artistas.repository.RegionalRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegionalService {

    private final RegionalRepository regionalRepository;
    private final RestTemplate restTemplate;
    private final String API_URL = "https://integrador-argus-api.geia.vip/v1/regionais";

    public RegionalService(RegionalRepository regionalRepository, RestTemplate restTemplate) {
        this.regionalRepository = regionalRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 3600000) // 1 hora
    @Transactional
    public void sincronizar() {
        try {
            RegionalDTO[] response = restTemplate.getForObject(API_URL, RegionalDTO[].class);
            
            // Se for null, será tratado como lista vazia para permitir a inativação dos locais
            List<RegionalDTO> dadosExternos = (response != null) ? Arrays.asList(response) : Collections.emptyList();

            Set<Long> idsExternosAtivos = dadosExternos.stream()
                    .map(RegionalDTO::getId)
                    .collect(Collectors.toSet());

            List<Regional> todasLocais = regionalRepository.findAll();
            
            // Mapa para busca rápida de registros ativos por ID Externo
            Map<Long, Regional> mapaAtivas = todasLocais.stream()
                    .filter(Regional::isAtivo)
                    .collect(Collectors.toMap(Regional::getIdExterno, r -> r, (r1, r2) -> r1));

            // 1. Inativar ausentes
            todasLocais.stream()
                    .filter(local -> local.isAtivo() && !idsExternosAtivos.contains(local.getIdExterno()))
                    .forEach(local -> {
                        local.setAtivo(false);
                        regionalRepository.save(local);
                    });

            // 2. Novos ou Alterados
            for (RegionalDTO dto : dadosExternos) {
                Regional existente = mapaAtivas.get(dto.getId());

                if (existente == null) {
                    inserirNova(dto);
                } else if (!existente.getNome().equals(dto.getNome())) {
                    existente.setAtivo(false);
                    regionalRepository.save(existente);
                    inserirNova(dto);
                }
            }
        } catch (Exception e) {
            System.err.println("Falha crítica na sincronização: " + e.getMessage());
        }
    }

    private void inserirNova(RegionalDTO dto) {
        Regional nova = Regional.builder()
                .idExterno(dto.getId())
                .nome(dto.getNome())
                .ativo(true)
                .build();
        regionalRepository.save(nova);
    }
}