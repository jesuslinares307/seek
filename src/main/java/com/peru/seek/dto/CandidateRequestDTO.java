package com.peru.seek.dto;

import java.math.BigDecimal;

public record CandidateRequestDTO(
        String name,
        String email,
        String gender,
        BigDecimal salaryExpected) {
}
