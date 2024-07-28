package com.peru.seek.controller;

import com.peru.seek.dto.CandidateRequestDTO;
import com.peru.seek.dto.CandidateResponseDTO;
import com.peru.seek.service.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateController {

    private final CandidateService candidateService;


    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CandidateResponseDTO> getCandidateById(@PathVariable Long id) {
        CandidateResponseDTO candidateResponseDTO = candidateService.getCandidateById(id);
        return (candidateResponseDTO != null) ? ResponseEntity.ok(candidateResponseDTO) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CandidateResponseDTO> createCandidate(@RequestBody CandidateRequestDTO candidateRequestDTO) {
        CandidateResponseDTO candidateResponseDTO = candidateService.createCandidate(candidateRequestDTO);
        return ResponseEntity.ok(candidateResponseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INVITED')")
    public ResponseEntity<CandidateResponseDTO> updateCandidate(
            @PathVariable Long id,
            @RequestBody CandidateRequestDTO candidateRequestDTO) {
        CandidateResponseDTO updatedCandidateResponseDTO = candidateService.updateCandidate(id, candidateRequestDTO);
        return (updatedCandidateResponseDTO != null) ? ResponseEntity.ok(updatedCandidateResponseDTO) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INVITED')")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        boolean deleted = candidateService.deleteCandidate(id);
        return (deleted) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
