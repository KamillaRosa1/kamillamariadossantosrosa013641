package com.projeto.api_artistas.controller;

import com.projeto.api_artistas.repository.RegionalRepository;
import com.projeto.api_artistas.security.JwtUtil;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegionalRepository repository;
    @Autowired private JwtUtil jwtUtil;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminAcessaRegionais() throws Exception {
        String token = jwtUtil.generateToken("admin@teste.com", 
            AuthorityUtils.createAuthorityList("ROLE_ADMIN"));

        mockMvc.perform(get("/api/v1/regionais")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
}

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("USER deve ser barrado em regionais (403)")
    void userBarradoEmRegionais() throws Exception {
        mockMvc.perform(get("/api/v1/regionais"))
                .andExpect(status().isForbidden());
    }
}