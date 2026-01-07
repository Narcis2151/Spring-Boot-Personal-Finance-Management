package org.fna.finance.mapper;

import org.fna.finance.dto.CategoryRequest;
import org.fna.finance.dto.CategoryResponse;
import org.fna.finance.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper implements ICategoryMapper {
    @Override
    public Category categoryRequestToCategory(CategoryRequest createCategoryRequest) {
        return new Category(createCategoryRequest.getName());
    }

    @Override
    public CategoryResponse categoryToCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

    @Override
    public List<CategoryResponse> categoriesToCategoriesResponse(List<Category> categories) {
        return categories.stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
}
