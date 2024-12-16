package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Registration Details")
public class RegisterRequest {
    @NotBlank()
    @Schema(description = "Full Name Of The User", example = "Test User")
    private String fullName;

    @NotBlank()
    @Email()
    @Schema(description = "Email Of The User", example = "test.user@mail.com")
    private String email;

    @NotBlank()
    @Schema(description = "Password Of The User", example = "password")
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String fullName, String password, String email) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
    }

}
