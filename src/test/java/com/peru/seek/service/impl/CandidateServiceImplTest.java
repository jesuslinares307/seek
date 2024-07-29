package com.peru.seek.service.impl;

import com.peru.seek.core.candidates.usecase.CandidateServiceImpl;
import com.peru.seek.core.candidates.model.CandidateRequestDTO;
import com.peru.seek.core.candidates.model.CandidateResponseDTO;
import com.peru.seek.core.candidates.usecase.exceptions.CandidateDuplicateException;
import com.peru.seek.core.candidates.usecase.exceptions.CandidateNotFoundException;
import com.peru.seek.core.candidates.port.mapper.CandidateMapper;
import com.peru.seek.infrastructure.outbound.repositories.candidates.entities.Candidate;
import com.peru.seek.infrastructure.outbound.repositories.candidates.CandidateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CandidateServiceImplTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Spy
    private CandidateMapper candidateMapper;

    @InjectMocks
    private CandidateServiceImpl candidateService;


    @Test
    @DisplayName("Obtiene un candidato por Id")
    void getCandidateById_CandidateFound() throws CandidateNotFoundException {
        Candidate candidate = new Candidate();
        candidate.setId(1L);

        CandidateResponseDTO candidateResponseDTO = getCandidateResponseDTO();
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(candidateMapper.apply(candidate)).thenReturn(candidateResponseDTO);

        CandidateResponseDTO result = candidateService.getCandidateById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("jesus", result.name());
        verify(candidateRepository).findById(1L);
        verify(candidateMapper).apply(candidate);
    }

    @Test
    @DisplayName("Devuelve una CandidateNotFoundException cuando se encuentra un candidato por el id solicitado")
    void getCandidateById_CandidateNotFound() {
        when(candidateRepository.findById(100L)).thenReturn(Optional.empty());

        CandidateNotFoundException exception = assertThrows(CandidateNotFoundException.class, () -> {
            candidateService.getCandidateById(100L);
        });
        assertEquals("Candidate not found with id: 100", exception.getMessage());
    }


    @Test
    @DisplayName("Obtiene una lista de todos los candidatos consultados")
    void getAllCandidates_WithCandidates() {
        Candidate candidate1 = new Candidate();
        candidate1.setId(1L);

        Candidate candidate2 = new Candidate();
        candidate2.setId(2L);

        CandidateResponseDTO candidateResponseDTO = getCandidateResponseDTO();
        CandidateResponseDTO secondCandidateResponseDTO = getSecondCandidateResponseDTO();


        when(candidateRepository.findAll()).thenReturn(List.of(candidate1, candidate2));
        when(candidateMapper.apply(candidate1)).thenReturn(candidateResponseDTO);
        when(candidateMapper.apply(candidate2)).thenReturn(secondCandidateResponseDTO);

        List<CandidateResponseDTO> result = candidateService.getAllCandidates();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());
        verify(candidateRepository).findAll();
        verify(candidateMapper).apply(candidate1);
        verify(candidateMapper).apply(candidate2);
    }

    @Test
    @DisplayName("Retorna una lista vacía cuando no hay candidatos")
    void getAllCandidates_EmptyList() {
        //mock
        when(candidateRepository.findAll()).thenReturn(Collections.emptyList());

        //call
        List<CandidateResponseDTO> result = candidateService.getAllCandidates();

        //assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        //verify
        verify(candidateRepository).findAll();
        verifyNoInteractions(candidateMapper);
    }

    @Test
    @DisplayName("Crea un candidato con exito")
    void createCandidate_CandidateCreatedSuccessfully() {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        CandidateRequestDTO candidateRequestDTO = getCandidateRequestDTO();
        CandidateResponseDTO candidateResponseDTO = getCandidateResponseDTO();

        when(candidateRepository.existsByEmail(candidateRequestDTO.email())).thenReturn(false);
        when(candidateRepository.save(any(Candidate.class))).thenReturn(candidate);
        when(candidateMapper.apply(candidate)).thenReturn(candidateResponseDTO);

        CandidateResponseDTO result = candidateService.createCandidate(candidateRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("jesus", result.name());
        assertEquals("jesus@example.com", result.email());
        assertEquals("male", result.gender());
        assertEquals(new BigDecimal("65.99"), result.salaryExpected());
        assertEquals(LocalDate.of(2024, 10, 12), result.createdAt());

        verify(candidateRepository).existsByEmail(candidateRequestDTO.email());
        verify(candidateRepository).save(any(Candidate.class));
        verify(candidateMapper).apply(candidate);
    }

    @Test
    @DisplayName("Retorna una CandidateDuplicateException cuando ya existe un candodado registrado con el mismo email")
    void createCandidate_CandidateAlreadyExists() {
        CandidateRequestDTO candidateRequestDTO = getCandidateRequestDTO();

        when(candidateRepository.existsByEmail(candidateRequestDTO.email())).thenReturn(true);

        CandidateDuplicateException exception = assertThrows(CandidateDuplicateException.class, () -> {
            candidateService.createCandidate(candidateRequestDTO);
        });
        assertEquals("Candidate with email " + candidateRequestDTO.email() + " already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Actualiza un candidato con exito")
    void updateCandidate_CandidateUpdatedSuccessfully() throws CandidateNotFoundException {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        CandidateRequestDTO candidateRequestDTO = getCandidateRequestDTO();
        CandidateResponseDTO candidateResponseDTO = getCandidateResponseDTO();

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(candidateRepository.save(any(Candidate.class))).thenReturn(candidate);
        when(candidateMapper.apply(candidate)).thenReturn(candidateResponseDTO);

        CandidateResponseDTO result = candidateService.updateCandidate(1L, candidateRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("jesus", result.name());
        assertEquals("jesus@example.com", result.email());
        assertEquals("male", result.gender());
        assertEquals(new BigDecimal("65.99"), result.salaryExpected());

        verify(candidateRepository).findById(1L);
        verify(candidateRepository).save(candidate);
        verify(candidateMapper).apply(candidate);
    }

    @Test
    @DisplayName("Retorna una exception tipo CandidateNotFoundException cuando no existe el candidato que se quiere actualizar")
    void updateCandidate_CandidateNotFound() {
        CandidateRequestDTO candidateRequestDTO = getCandidateRequestDTO();
        when(candidateRepository.findById(100L)).thenReturn(Optional.empty());

        CandidateNotFoundException exception = assertThrows(CandidateNotFoundException.class, () -> {
            candidateService.updateCandidate(100L, candidateRequestDTO);
        });
        assertEquals("Candidate not found with id: 100", exception.getMessage());
    }

    @Test
    @DisplayName("Elimina un candidato con exito")
    void deleteCandidate_CandidateDeletedSuccessfully() throws CandidateNotFoundException {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("JESUS");
        candidate.setEmail("jesuslinares307@gmail.com");
        candidate.setGender("M");
        candidate.setSalaryExpected(BigDecimal.valueOf(3500.00));
        candidate.setCreatedAt(LocalDate.now());

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

        candidateService.deleteCandidate(1L);

        verify(candidateRepository).findById(1L);
        verify(candidateRepository).deleteById(1L);

    }

    @Test
    @DisplayName("Candidato a eliminar no encontrado")
    void deleteCandidate_CandidateNotFound() throws CandidateNotFoundException {
        when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class, () -> {
            candidateService.deleteCandidate(1L);
        });
        verify(candidateRepository).findById(1L);
        verify(candidateRepository, never()).deleteById(1L);
    }

    private static CandidateResponseDTO getCandidateResponseDTO() {
        return new CandidateResponseDTO(1L, "jesus",
                "jesus@example.com", "male", new BigDecimal("65.99"), LocalDate.of(2024, 10, 12));
    }

    private static CandidateResponseDTO getSecondCandidateResponseDTO() {
        return new CandidateResponseDTO(2L, "adolfo",
                "adolfo@gmail.com", "male", new BigDecimal("9999.99"), LocalDate.of(2024, 12, 16));
    }

    private static CandidateRequestDTO getCandidateRequestDTO() {
        return new CandidateRequestDTO("jesus", "jesus@example.com", "male", new BigDecimal("65.99"));
    }

}