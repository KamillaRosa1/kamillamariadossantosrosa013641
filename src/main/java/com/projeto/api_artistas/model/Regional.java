package com.projeto.api_artistas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "regionais")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Regional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_externo", nullable = false)
    @EqualsAndHashCode.Include // Usamos o ID externo para comparar se é a mesma regional
    private Long idExterno;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private boolean ativo;
}