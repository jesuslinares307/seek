package com.peru.seek.service;

import com.peru.seek.dto.CandidateRequestDTO;
import com.peru.seek.dto.CandidateResponseDTO;

import java.util.List;

public interface CandidateService {

    List<CandidateResponseDTO> getAllCandidates();

    CandidateResponseDTO getCandidateById(Long id);

    CandidateResponseDTO createCandidate(CandidateRequestDTO candidateRequestDTO);

    CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO candidateRequestDTO);

    boolean deleteCandidate(Long id);
}
