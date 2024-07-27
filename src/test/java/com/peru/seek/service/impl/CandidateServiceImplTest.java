package com.peru.seek.service.impl;

import com.peru.seek.dto.CandidateRequestDTO;
import com.peru.seek.dto.CandidateResponseDTO;
import com.peru.seek.exceptions.CandidateDuplicateException;
import com.peru.seek.exceptions.CandidateNotFoundException;
import com.peru.seek.mapper.CandidateMapper;
import com.peru.seek.model.Candidate;
import com.peru.seek.repository.CandidateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Mock
    private CandidateMapper candidateMapper;

    @InjectMocks
    private CandidateServiceImpl candidateService;


    @Test
    void getCandidateById_CandidateFound() {
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
    void getCandidateById_NullId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            candidateService.getCandidateById(null);
        });
        assertEquals("Invalid ID provided: null", exception.getMessage());
    }

    @Test
    void getCandidateById_CandidateNotFound() {
        when(candidateRepository.findById(100L)).thenReturn(Optional.empty());

        CandidateNotFoundException exception = assertThrows(CandidateNotFoundException.class, () -> {
            candidateService.getCandidateById(100L);
        });
        assertEquals("Candidate not found with id: 100", exception.getMessage());
    }


    @Test
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
    @DisplayName("")
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
    void createCandidate_CandidateAlreadyExists() {
        CandidateRequestDTO candidateRequestDTO = getCandidateRequestDTO();

        when(candidateRepository.existsByEmail(candidateRequestDTO.email())).thenReturn(true);

        CandidateDuplicateException exception = assertThrows(CandidateDuplicateException.class, () -> {
            candidateService.createCandidate(candidateRequestDTO);
        });
        assertEquals("Candidate with email " + candidateRequestDTO.email() + " already exists", exception.getMessage());
    }

    @Test
    void updateCandidate_CandidateUpdatedSuccessfully() {
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
    void updateCandidate_CandidateNotFound() {
        CandidateRequestDTO candidateRequestDTO = getCandidateRequestDTO();
        when(candidateRepository.findById(100L)).thenReturn(Optional.empty());

        CandidateNotFoundException exception = assertThrows(CandidateNotFoundException.class, () -> {
            candidateService.updateCandidate(100L, candidateRequestDTO);
        });
        assertEquals("Candidate not found with id: 100", exception.getMessage());
    }

    @Test
    void deleteCandidate_CandidateDeletedSuccessfully() {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

        boolean result = candidateService.deleteCandidate(1L);

        assertTrue(result);
        verify(candidateRepository).findById(1L);
        verify(candidateRepository).delete(candidate);
    }

    @Test
    void deleteCandidate_CandidateNotFound() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = candidateService.deleteCandidate(1L);

        assertFalse(result);
        verify(candidateRepository).findById(1L);
        verify(candidateRepository, never()).delete(any(Candidate.class));
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