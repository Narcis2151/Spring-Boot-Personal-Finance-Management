package org.fna.finance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.fna.finance.dto.LoginResponse;
import org.fna.finance.service.AuthenticationService;
import org.fna.finance.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.fna.finance.dto.LoginRequest;
import org.fna.finance.dto.RegisterRequest;
import org.fna.finance.model.User;

@RequestMapping("/auth")
@RestController
@Tag(name = "Authentication", description = "Endpoints for managing user authentication")
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/signup")
    @Operation(summary = "Sign Up User", description = "Register a new user")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest registerUserDto) {
        authenticationService.signup(registerUserDto);

        User authenticatedUser = authenticationService.authenticate(
                new LoginRequest(registerUserDto.getEmail(), registerUserDto.getPassword())
        );

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(path = "/login")
    @Operation(summary = "Login User", description = "Authenticate a user")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
