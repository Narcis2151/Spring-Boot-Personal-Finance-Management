package org.fna.finance.service;

import org.fna.finance.dto.LoginRequest;
import org.fna.finance.dto.RegisterRequest;
import org.fna.finance.exception.DuplicateEmailException;
import org.fna.finance.exception.InvalidCredentialsException;
import org.fna.finance.model.User;

public interface IAuthenticationService {
    User signup(RegisterRequest input) throws DuplicateEmailException;

    User authenticate(LoginRequest input) throws InvalidCredentialsException;
}
