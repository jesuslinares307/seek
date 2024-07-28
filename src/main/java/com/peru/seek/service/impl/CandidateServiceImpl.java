package com.peru.seek.service.impl;

import com.peru.seek.dto.CandidateRequestDTO;
import com.peru.seek.dto.CandidateResponseDTO;
import com.peru.seek.exceptions.CandidateDuplicateException;
import com.peru.seek.exceptions.CandidateNotFoundException;
import com.peru.seek.mapper.CandidateMapper;
import com.peru.seek.model.Candidate;
import com.peru.seek.repository.CandidateRepository;
import com.peru.seek.service.CandidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CandidateServiceImpl implements CandidateService {
    private static final Logger logger = LoggerFactory.getLogger(CandidateServiceImpl.class);

    private final CandidateRepository candidateRepository;

    private final CandidateMapper candidateMapper;

    public CandidateServiceImpl(CandidateRepository candidateRepository, CandidateMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }

    @Override
    public List<CandidateResponseDTO> getAllCandidates() {
        logger.info("Fetching all candidates");
        return candidateRepository.findAll().stream()
                .map(candidateMapper)
                .collect(Collectors.toList());
    }

    @Override
    public CandidateResponseDTO getCandidateById(Long id) throws CandidateNotFoundException {
        logger.info("Fetching candidate with id: {}", id);
        return candidateRepository.findById(id)
                .map(candidateMapper)
                .orElseThrow(() -> new CandidateNotFoundException("Candidate not found with id: " + id));
    }

    @Override
    public CandidateResponseDTO createCandidate(CandidateRequestDTO candidateRequestDTO) {
        logger.info("Creating candidate with email: {}", candidateRequestDTO.email());
        if (candidateRepository.existsByEmail(candidateRequestDTO.email())) {
            logger.error("Candidate with email {} already exists", candidateRequestDTO.email());
            throw new CandidateDuplicateException("Candidate with email " + candidateRequestDTO.email() + " already exists");
        }
        Candidate candidate = new Candidate();
        candidate.setName(candidateRequestDTO.name());
        candidate.setEmail(candidateRequestDTO.email());
        candidate.setGender(candidateRequestDTO.gender());
        candidate.setSalaryExpected(candidateRequestDTO.salaryExpected());
        candidate.setCreatedAt(LocalDate.now());

        Candidate savedCandidate = candidateRepository.save(candidate);
        logger.info("Candidate created with id: {}", savedCandidate.getId());
        return candidateMapper.apply(savedCandidate);
    }

    @Override
    public CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO candidateRequestDTO) throws CandidateNotFoundException {
        logger.info("Updating candidate with id: {}", id);
        return candidateRepository.findById(id)
                .map(candidate -> {
                    candidate.setName(candidateRequestDTO.name());
                    candidate.setEmail(candidateRequestDTO.email());
                    candidate.setGender(candidateRequestDTO.gender());
                    candidate.setSalaryExpected(candidateRequestDTO.salaryExpected());
                    Candidate updatedCandidate = candidateRepository.save(candidate);
                    logger.info("Candidate updated with id: {}", id);
                    return candidateMapper.apply(updatedCandidate);
                })
                .orElseThrow(() -> new CandidateNotFoundException("Candidate not found with id: " + id));
    }

    @Override
    public void deleteCandidate(Long id) throws CandidateNotFoundException {
        logger.info("Deleting candidate with id: {}", id);
        CandidateResponseDTO candidate = this.getCandidateById(id);
        candidateRepository.deleteById(candidate.id());
        logger.info("Candidate deleted with id: {}", id);
    }
}