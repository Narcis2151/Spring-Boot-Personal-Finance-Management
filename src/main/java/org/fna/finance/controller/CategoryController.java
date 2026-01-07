package org.fna.finance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.fna.finance.dto.*;
import org.fna.finance.mapper.CategoryMapper;
import org.fna.finance.model.Category;
import org.fna.finance.model.User;
import org.fna.finance.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/category")
@RestController
@Tag(name = "Categories", description = "Endpoints for managing categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Get all categories for the authenticated user")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categoryMapper.categoriesToCategoriesResponse(
                categoryService.getAllCategories(user))
        );
    }

    @PostMapping
    @Operation(summary = "Create category", description = "Create a new category")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest,
                                                           @AuthenticationPrincipal User user) {
        Category category = categoryMapper.categoryRequestToCategory(categoryRequest);
        category.setUser(user);
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(categoryMapper.categoryToCategoryResponse(createdCategory));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category", description = "Get a category by id")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categoryMapper.categoryToCategoryResponse(
                categoryService.getCategory(user, id))
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category name", description = "Update the name of a category")
    public ResponseEntity<CategoryResponse> updateCategoryName(@PathVariable Long id,
                                                               @Valid @RequestBody CategoryRequest categoryRequest,
                                                               @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categoryMapper.categoryToCategoryResponse(
                categoryService.updateCategoryName(user, id, categoryRequest.getName()))
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete a category by id")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id,
                                               @AuthenticationPrincipal User user) {
        categoryService.deleteCategory(user, id);
        return ResponseEntity.noContent().build();
    }

}
