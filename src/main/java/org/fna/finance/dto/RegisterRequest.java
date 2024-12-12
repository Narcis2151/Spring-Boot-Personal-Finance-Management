package org.fna.finance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {
    @NotBlank()
    private String fullName;

    @NotBlank()
    @Email()
    private String email;

    @NotBlank()
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String fullName, String password, String email) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
    }

}
