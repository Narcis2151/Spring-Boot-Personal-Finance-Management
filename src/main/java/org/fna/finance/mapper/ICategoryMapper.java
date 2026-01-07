package org.fna.finance.mapper;

import org.fna.finance.dto.CategoryRequest;
import org.fna.finance.dto.CategoryResponse;
import org.fna.finance.model.Category;

import java.util.List;

public interface ICategoryMapper {
    Category categoryRequestToCategory(CategoryRequest createCategoryRequest);

    CategoryResponse categoryToCategoryResponse(Category category);

    List<CategoryResponse> categoriesToCategoriesResponse(List<Category> categories);
}
