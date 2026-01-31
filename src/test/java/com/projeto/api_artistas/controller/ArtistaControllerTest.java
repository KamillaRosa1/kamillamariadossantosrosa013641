package com.projeto.api_artistas.controller;

import com.projeto.api_artistas.model.Artista;
import com.projeto.api_artistas.service.ArtistaService;
import com.projeto.api_artistas.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArtistaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtistaService service;
    @Autowired private JwtUtil jwtUtil;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("ADMIN deve conseguir cadastrar novo artista")
    void adminDeveCadastrarArtista() throws Exception {
        String token = jwtUtil.generateToken("admin@teste.com", 
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_ADMIN"));

        String json = "{\"nome\": \"Arctic Monkeys\", \"tipo\": \"BANDA\"}";
        when(service.salvar(any(Artista.class))).thenReturn(new Artista());

        mockMvc.perform(post("/api/v1/artistas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("USER não deve conseguir cadastrar artista (403)")
    void userNaoDeveCadastrarArtista() throws Exception {
        String json = "{\"nome\": \"Forbidden Artist\", \"tipo\": \"CANTOR\"}";

        mockMvc.perform(post("/api/v1/artistas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().isForbidden()); 
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("USER deve conseguir listar artistas")
    void userDeveListarArtistas() throws Exception {
        // Gera o token para o USER
        String token = jwtUtil.generateToken("user@teste.com", 
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_USER"));

        mockMvc.perform(get("/api/v1/artistas")
                .header("Authorization", "Bearer " + token)) 
                .andExpect(status().isOk());
    }
}