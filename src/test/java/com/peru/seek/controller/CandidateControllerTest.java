package com.peru.seek.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peru.seek.core.candidates.model.CandidateRequestDTO;
import com.peru.seek.core.candidates.model.CandidateResponseDTO;
import com.peru.seek.core.candidates.usecase.exceptions.CandidateNotFoundException;
import com.peru.seek.core.candidates.port.CandidateService;
import com.peru.seek.infrastructure.CandidatesApplication;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CandidatesApplication.class)
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

    public static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKZXN1cyIsImlhdCI6MTcyMjE4MzI3OSwiZXhwIjoxNzI1MzM2ODc5fQ.d91-6U-sNHS-aEFecPkGeEvrtzcSrw_sCHdpudQ6rJU";


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
        doThrow(new CandidateNotFoundException("Candidate not found")).when(candidateService).getCandidateById(1L);

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
        doThrow(new CandidateNotFoundException("Candidate not found")).when(candidateService).updateCandidate(eq(1L), any(CandidateRequestDTO.class));

        mockMvc.perform(put("/api/v1/candidates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidateRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCandidate() throws Exception {
        doNothing().when(candidateService).deleteCandidate(1L);

        mockMvc.perform(delete("/api/v1/candidates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCandidate_NotFound() throws Exception {
        doThrow(new CandidateNotFoundException("Candidate not found")).when(candidateService).deleteCandidate(1L);

        mockMvc.perform(delete("/api/v1/candidates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
