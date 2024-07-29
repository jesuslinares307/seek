package com.peru.seek.core.candidates.model;

import java.math.BigDecimal;

public record CandidateRequestDTO(
        String name,
        String email,
        String gender,
        BigDecimal salaryExpected) {
}
