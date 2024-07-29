package com.peru.seek.core.candidates.port.mapper;

import com.peru.seek.infrastructure.outbound.repositories.candidates.entities.Candidate;
import com.peru.seek.core.candidates.model.CandidateResponseDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CandidateMapper implements Function<Candidate, CandidateResponseDTO> {

    @Override
    public CandidateResponseDTO apply(Candidate entity) {
        return new CandidateResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getGender(),
                entity.getSalaryExpected(),
                entity.getCreatedAt()
        );
    }

}
