package org.fna.finance.service;

import org.fna.finance.exception.CategoryNotFoundException;
import org.fna.finance.model.Category;
import org.fna.finance.model.User;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories(User user);

    Category createCategory(Category category);

    Category getCategory(User user, Long id) throws CategoryNotFoundException;

    Category updateCategoryName(User user, Long id, String name) throws CategoryNotFoundException;

    void deleteCategory(User user, Long id) throws CategoryNotFoundException;
}
