package com.peru.seek.controller;

import com.peru.seek.dto.CandidateRequestDTO;
import com.peru.seek.dto.CandidateResponseDTO;
import com.peru.seek.exceptions.CandidateNotFoundException;
import com.peru.seek.exceptions.ErrorResponse;
import com.peru.seek.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
@Validated
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los candidatos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtener todos los candidatos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un candidato por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtener Candidato"),
            @ApiResponse(responseCode = "404", description = "Candidato no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CandidateResponseDTO> getCandidateById(@PathVariable @NotNull @Min(1) Long id) throws CandidateNotFoundException {
        CandidateResponseDTO candidateResponseDTO = candidateService.getCandidateById(id);
        return ResponseEntity.ok(candidateResponseDTO);
    }

    @PostMapping
    @Operation(summary = "Crear un candidato exitosamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crear Candidato"),
            @ApiResponse(responseCode = "409", description = "Candidate con este correo electronico ya existe registrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<CandidateResponseDTO> createCandidate(@RequestBody CandidateRequestDTO candidateRequestDTO) {
        CandidateResponseDTO candidateResponseDTO = candidateService.createCandidate(candidateRequestDTO);
        return ResponseEntity.ok(candidateResponseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un candidato por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato Actualizado"),
            @ApiResponse(responseCode = "404", description = "Candidato no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<CandidateResponseDTO> updateCandidate(
            @PathVariable Long id,
            @RequestBody CandidateRequestDTO candidateRequestDTO) throws CandidateNotFoundException {
        CandidateResponseDTO updatedCandidateResponseDTO = candidateService.updateCandidate(id, candidateRequestDTO);
        return ResponseEntity.ok(updatedCandidateResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un candidato por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Candidato eliminado"),
            @ApiResponse(responseCode = "404", description = "Candidato no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) throws CandidateNotFoundException {
        candidateService.deleteCandidate(id);
        return ResponseEntity.ok().build();
    }
}
