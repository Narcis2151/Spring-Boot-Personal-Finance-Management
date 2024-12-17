package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Category Response Details")
public class CategoryResponse {
    @Schema(description = "Category Id", example = "1")
    private Long id;

    @Schema(description = "Category Name", example = "Food")
    private String name;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
