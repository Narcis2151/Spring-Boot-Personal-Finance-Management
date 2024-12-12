package org.fna.finance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    @NotBlank()
    @Email()
    private String email;

    @NotBlank()
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }

}
