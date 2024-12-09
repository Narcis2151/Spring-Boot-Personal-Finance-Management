package org.fna.finance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class LoginRequest {

    @NotBlank()
    @Email()
    private String email;

    @NotBlank()
    @Length(min = 6)
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
