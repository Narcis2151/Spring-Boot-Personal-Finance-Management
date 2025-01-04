package org.fna.finance.service;

import org.fna.finance.dto.LoginRequest;
import org.fna.finance.dto.RegisterRequest;
import org.fna.finance.exception.DuplicateEmailException;
import org.fna.finance.exception.InvalidCredentialsException;
import org.fna.finance.model.User;
import org.fna.finance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_Success() throws DuplicateEmailException {
        RegisterRequest registerRequest = new RegisterRequest("John Doe", "password123", "john.doe@example.com");
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = authenticationService.signup(registerRequest);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_DuplicateEmailException() {
        RegisterRequest registerRequest = new RegisterRequest("John Doe", "password123", "john.doe@example.com");

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> authenticationService.signup(registerRequest));

        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void authenticate_Success() throws InvalidCredentialsException {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password123");
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));

        authenticationService.authenticate(loginRequest);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        verify(userRepository).findByEmail(loginRequest.getEmail());
    }

    @Test
    void authenticate_InvalidCredentialsException_InvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "wrongPassword");

        doThrow(new RuntimeException()).when(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.authenticate(loginRequest));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        verifyNoInteractions(userRepository);
    }

    @Test
    void authenticate_InvalidCredentialsException_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest("unknown@example.com", "password123");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.authenticate(loginRequest));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        verify(userRepository).findByEmail(loginRequest.getEmail());
    }
}
