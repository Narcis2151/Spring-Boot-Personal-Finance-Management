package org.fna.finance.service;

import org.fna.finance.dto.LoginRequest;
import org.fna.finance.dto.RegisterRequest;
import org.fna.finance.exception.DuplicateEmailException;
import org.fna.finance.exception.InvalidCredentialsException;
import org.fna.finance.repository.UserRepository;
import org.fna.finance.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterRequest input) throws DuplicateEmailException {
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new DuplicateEmailException();
        }
        User user = new User(
                input.getFullName(),
                passwordEncoder.encode(input.getPassword()),
                input.getEmail()
        );

        return userRepository.save(user);
    }

    public User authenticate(LoginRequest input) throws InvalidCredentialsException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
        return userRepository.findByEmail(input.getEmail()).orElseThrow(InvalidCredentialsException::new);

    }
}
