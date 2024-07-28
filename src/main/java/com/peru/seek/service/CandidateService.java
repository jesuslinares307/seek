package com.peru.seek.service;

import com.peru.seek.dto.CandidateRequestDTO;
import com.peru.seek.dto.CandidateResponseDTO;
import com.peru.seek.exceptions.CandidateNotFoundException;

import java.util.List;

public interface CandidateService {

    List<CandidateResponseDTO> getAllCandidates();

    CandidateResponseDTO getCandidateById(Long id) throws CandidateNotFoundException;

    CandidateResponseDTO createCandidate(CandidateRequestDTO candidateRequestDTO);

    CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO candidateRequestDTO) throws CandidateNotFoundException;

    void deleteCandidate(Long id) throws CandidateNotFoundException;
}
