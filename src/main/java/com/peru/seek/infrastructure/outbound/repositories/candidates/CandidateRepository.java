package com.peru.seek.infrastructure.outbound.repositories.candidates;

import com.peru.seek.infrastructure.outbound.repositories.candidates.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    boolean existsByEmail(String email);
}
