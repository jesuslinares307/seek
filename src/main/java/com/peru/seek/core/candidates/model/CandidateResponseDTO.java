package com.peru.seek.core.candidates.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CandidateResponseDTO(
        Long id,
        String name,
        String email,
        String gender,
        BigDecimal salaryExpected,
        LocalDate createdAt
) {
}
