package com.projeto.api_artistas.controller;

import com.projeto.api_artistas.service.AlbumService;
import com.projeto.api_artistas.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AlbumControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private AlbumService albumService;
    
    // Injeção do componente real para gerar o token válido
    @Autowired private JwtUtil jwtUtil;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveFazerUploadComSucessoComoAdmin() throws Exception {
        // Criando a autoridade compatível com generateToken
        String token = jwtUtil.generateToken("admin@teste.com", 
                AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "bytes".getBytes());
        when(albumService.uploadCapa(eq(1L), any())).thenReturn("http://s3.com/link");

        mockMvc.perform(multipart("/api/v1/albuns/1/capa")
                .file(file)
                .header("Authorization", "Bearer " + token)
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deveRetornarForbiddenParaUsuarioSemRoleAdmin() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "bytes".getBytes());
        
        mockMvc.perform(multipart("/api/v1/albuns/1/capa").file(file).with(csrf()))
                .andExpect(status().isForbidden()); 
    }

    @Test
    @WithMockUser(username = "spam_user", roles = "ADMIN")
    void deveValidarRateLimitAposDezRequisicoes() throws Exception {
        String token = jwtUtil.generateToken("spam_user", 
                AuthorityUtils.createAuthorityList("ROLE_ADMIN"));

        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/api/v1/albuns")
                    .header("Authorization", "Bearer " + token)
                    .param("page", "0").param("size", "1"));
        }
        mockMvc.perform(get("/api/v1/albuns")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isTooManyRequests());
    }
}