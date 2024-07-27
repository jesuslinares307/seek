package com.peru.seek.mapper;

import com.peru.seek.model.Candidate;
import com.peru.seek.dto.CandidateResponseDTO;
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
