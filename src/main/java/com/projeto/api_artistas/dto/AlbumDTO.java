package com.projeto.api_artistas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    private Long id;
    private String titulo;
    private String imagemCapaUrl;
    private Set<Long> artistas; 
}