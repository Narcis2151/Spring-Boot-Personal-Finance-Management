package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Create Account Details")
public class CreateAccountRequest {
    @NotBlank
    @Schema(description = "Account Name", example = "Test Account")
    private String name;

    @NotNull
    @Schema(description = "Account Currency", example = "RON")
    private String currency;

    @Positive
    @Schema(description = "Account Balance", example = "1000")
    private Double balance;

}
