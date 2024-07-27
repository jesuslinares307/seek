package com.peru.seek.controller;

import com.peru.seek.dto.CandidateRequestDTO;
import com.peru.seek.dto.CandidateResponseDTO;
import com.peru.seek.service.CandidateService;
import org.springframework.http.ResponseEntity;
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
    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> getCandidateById(@PathVariable Long id) {
        CandidateResponseDTO candidateResponseDTO = candidateService.getCandidateById(id);
        return (candidateResponseDTO != null) ? ResponseEntity.ok(candidateResponseDTO) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CandidateResponseDTO> createCandidate(@RequestBody CandidateRequestDTO candidateRequestDTO) {
        CandidateResponseDTO candidateResponseDTO = candidateService.createCandidate(candidateRequestDTO);
        return ResponseEntity.ok(candidateResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> updateCandidate(
            @PathVariable Long id,
            @RequestBody CandidateRequestDTO candidateRequestDTO) {
        CandidateResponseDTO updatedCandidateResponseDTO = candidateService.updateCandidate(id, candidateRequestDTO);
        return (updatedCandidateResponseDTO != null) ? ResponseEntity.ok(updatedCandidateResponseDTO) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        boolean deleted = candidateService.deleteCandidate(id);
        return (deleted) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
