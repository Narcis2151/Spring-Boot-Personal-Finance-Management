package org.fna.finance.controller;

import org.fna.finance.dto.LoginRequest;
import org.fna.finance.dto.RegisterRequest;
import org.fna.finance.exception.DuplicateEmailException;
import org.fna.finance.exception.InvalidCredentialsException;
import org.fna.finance.model.User;
import org.fna.finance.service.AuthenticationService;
import org.fna.finance.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testRegister() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        User registeredUser = new User();
        registeredUser.setId(1L);
        registeredUser.setEmail("test@example.com");

        User authenticatedUser = new User();
        authenticatedUser.setId(1L);
        authenticatedUser.setEmail("test@example.com");

        when(authenticationService.signup(registerRequest)).thenReturn(registeredUser);
        when(authenticationService.authenticate(any(LoginRequest.class))).thenReturn(authenticatedUser);
        when(jwtService.generateToken(authenticatedUser)).thenReturn("testToken");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"Test User\", \"email\": \"test@example.com\", \"password\": \"password\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testToken"))
                .andExpect(jsonPath("$.expiresIn").value(3600L));
    }

    @Test
    @WithMockUser
    void testAuthenticate() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        User authenticatedUser = new User();
        authenticatedUser.setId(1L);
        authenticatedUser.setEmail("test@example.com");

        when(authenticationService.authenticate(loginRequest)).thenReturn(authenticatedUser);
        when(jwtService.generateToken(authenticatedUser)).thenReturn("testToken");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expiresIn").value(3600L));
    }

    @Test
    @WithMockUser
    void testRegister_duplicateEmail() throws Exception {
        when(authenticationService.signup(any(RegisterRequest.class))).thenThrow(new DuplicateEmailException());

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"Test User\", \"email\": \"test@example.com\", \"password\": \"password\"}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Account with this email already exists."));
    }

    @Test
    @WithMockUser
    void testAuthenticate_invalidCredentials() throws Exception {
        when(authenticationService.authenticate(any(LoginRequest.class))).thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\"}")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email or password is incorrect."));
    }

    @Test
    @WithMockUser
    void testRegister_badRequest() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest());
    }
}