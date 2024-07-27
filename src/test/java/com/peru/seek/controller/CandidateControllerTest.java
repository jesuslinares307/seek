package com.peru.seek.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peru.seek.dto.CandidateRequestDTO;
import com.peru.seek.dto.CandidateResponseDTO;
import com.peru.seek.service.CandidateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;

    @Autowired
    private ObjectMapper objectMapper;

    private CandidateResponseDTO candidateResponseDTO;
    private CandidateRequestDTO candidateRequestDTO;

    public static final String TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKZXN1cyIsImV4cCI6MTcyMjExNjg4OSwiaWF0IjoxNzIyMTEzMjg5fQ.YwaOVTPwRQQ-ILWQB7V0cPztBTOFUinRf_3yKAz1u_yvZ0zw0t2Q2YGhVRauE6omZMVwwkOWkBr-rTslN5Mr8A";


    @BeforeEach
    void setUp() {
        candidateRequestDTO = new CandidateRequestDTO("John Doe", "john.doe@example.com", "Male", new BigDecimal("65.99"));

        candidateResponseDTO = new CandidateResponseDTO(1L, "jesus",
                "jesus@example.com", "male", new BigDecimal("65.99"), LocalDate.of(2024, 10, 12));
    }

    @Test
    void getCandidateById() throws Exception {
        given(candidateService.getCandidateById(1L)).willReturn(candidateResponseDTO);

        mockMvc.perform(get("/api/v1/candidates/1")
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCandidateById_NotFound() throws Exception {
        given(candidateService.getCandidateById(1L)).willReturn(null);

        mockMvc.perform(get("/api/v1/candidates/1")
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCandidates() throws Exception {
        given(candidateService.getAllCandidates()).willReturn(List.of(candidateResponseDTO));

        mockMvc.perform(get("/api/v1/candidates")
                        .header(AUTHORIZATION, TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    void createCandidate() throws Exception {
        given(candidateService.createCandidate(any(CandidateRequestDTO.class))).willReturn(candidateResponseDTO);

        mockMvc.perform(post("/api/v1/candidates")
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidateRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateCandidate() throws Exception {
        given(candidateService.updateCandidate(eq(1L), any(CandidateRequestDTO.class))).willReturn(candidateResponseDTO);

        mockMvc.perform(put("/api/v1/candidates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidateRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateCandidate_NotFound() throws Exception {
        given(candidateService.updateCandidate(eq(1L), any(CandidateRequestDTO.class))).willReturn(null);

        mockMvc.perform(put("/api/v1/candidates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidateRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCandidate() throws Exception {
        given(candidateService.deleteCandidate(1L)).willReturn(true);

        mockMvc.perform(delete("/api/v1/candidates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCandidate_NotFound() throws Exception {
        given(candidateService.deleteCandidate(1L)).willReturn(false);

        mockMvc.perform(delete("/api/v1/candidates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
