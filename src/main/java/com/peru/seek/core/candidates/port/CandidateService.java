package com.peru.seek.core.candidates.port;

import com.peru.seek.core.candidates.model.CandidateRequestDTO;
import com.peru.seek.core.candidates.model.CandidateResponseDTO;
import com.peru.seek.core.candidates.usecase.exceptions.CandidateNotFoundException;

import java.util.List;

public interface CandidateService {

    List<CandidateResponseDTO> getAllCandidates();

    CandidateResponseDTO getCandidateById(Long id) throws CandidateNotFoundException;

    CandidateResponseDTO createCandidate(CandidateRequestDTO candidateRequestDTO);

    CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO candidateRequestDTO) throws CandidateNotFoundException;

    void deleteCandidate(Long id) throws CandidateNotFoundException;
}
