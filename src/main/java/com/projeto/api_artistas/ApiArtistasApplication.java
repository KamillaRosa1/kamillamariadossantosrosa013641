package com.projeto.api_artistas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/* @EnableScheduling ativa o suporte para a execução periódica 
 * do RegionalService.sincronizar().
 */
@SpringBootApplication
@EnableScheduling
public class ApiArtistasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiArtistasApplication.class, args);
    }
}