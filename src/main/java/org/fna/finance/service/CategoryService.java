package org.fna.finance.service;

import org.fna.finance.exception.CategoryNotFoundException;
import org.fna.finance.model.Category;
import org.fna.finance.model.User;
import org.fna.finance.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories(User user) {
        return categoryRepository.findAllByUser(user);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategory(User user, Long id) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findByUserAndId(user, id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new CategoryNotFoundException(id);
        }
    }

    @Override
    public Category updateCategoryName(User user, Long id, String name) throws CategoryNotFoundException {
        Category category = categoryRepository.findByUserAndId(user, id).orElse(null);
        if (category != null) {
            category.setName(name);
            return categoryRepository.save(category);
        } else {
            throw new CategoryNotFoundException(id);
        }
    }

    @Override
    public void deleteCategory(User user, Long id) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findByUserAndId(user, id);
        if (category.isPresent()) {
            categoryRepository.delete(category.get());
        } else {
            throw new CategoryNotFoundException(id);
        }
    }


}
