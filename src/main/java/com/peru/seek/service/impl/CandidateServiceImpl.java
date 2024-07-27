package com.peru.seek.service.impl;


import com.peru.seek.dto.CandidateRequestDTO;
import com.peru.seek.dto.CandidateResponseDTO;
import com.peru.seek.exceptions.CandidateDuplicateException;
import com.peru.seek.exceptions.CandidateNotFoundException;
import com.peru.seek.mapper.CandidateMapper;
import com.peru.seek.model.Candidate;
import com.peru.seek.repository.CandidateRepository;
import com.peru.seek.service.CandidateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;

    private final CandidateMapper candidateMapper;

    public CandidateServiceImpl(CandidateRepository candidateRepository, CandidateMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }

    @Override
    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(candidateMapper)
                .collect(Collectors.toList());
    }

    @Override
    public CandidateResponseDTO getCandidateById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID provided: " + id);
        }
        return candidateRepository.findById(id)
                .map(candidateMapper)
                .orElseThrow(() -> new CandidateNotFoundException("Candidate not found with id: " + id));
    }

    @Override
    public CandidateResponseDTO createCandidate(CandidateRequestDTO candidateRequestDTO) {
        if (candidateRepository.existsByEmail(candidateRequestDTO.email())) {
            throw new CandidateDuplicateException("Candidate with email " + candidateRequestDTO.email() + " already exists");
        }
        Candidate candidate = new Candidate();
        candidate.setName(candidateRequestDTO.name());
        candidate.setEmail(candidateRequestDTO.email());
        candidate.setGender(candidateRequestDTO.gender());
        candidate.setSalaryExpected(candidateRequestDTO.salaryExpected());
        candidate.setCreatedAt(LocalDate.now());

        Candidate savedCandidate = candidateRepository.save(candidate);
        return candidateMapper.apply(savedCandidate);
    }

    @Override
    public CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO candidateRequestDTO) {
        return candidateRepository.findById(id)
                .map(candidate -> {
                    candidate.setName(candidateRequestDTO.name());
                    candidate.setEmail(candidateRequestDTO.email());
                    candidate.setGender(candidateRequestDTO.gender());
                    candidate.setSalaryExpected(candidateRequestDTO.salaryExpected());
                    Candidate updatedCandidate = candidateRepository.save(candidate);
                    return candidateMapper.apply(updatedCandidate);
                })
                .orElseThrow(() -> new CandidateNotFoundException("Candidate not found with id: " + id));
    }

    @Override
    public boolean deleteCandidate(Long id) {
        return candidateRepository.findById(id)
                .map(candidate -> {
                    candidateRepository.delete(candidate);
                    return true;
                })
                .orElse(false);
    }
}
