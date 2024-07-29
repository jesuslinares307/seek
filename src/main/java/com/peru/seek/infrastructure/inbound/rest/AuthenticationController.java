package com.peru.seek.infrastructure.inbound.rest;

import com.peru.seek.infrastructure.inbound.rest.exceptions.ErrorResponse;
import com.peru.seek.security.JwtService;
import com.peru.seek.security.dto.AuthenticationRequest;
import com.peru.seek.security.dto.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Obtener un token valido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtener un token Valido"),
            @ApiResponse(responseCode = "500", description = "Credenciales Incorrectas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}