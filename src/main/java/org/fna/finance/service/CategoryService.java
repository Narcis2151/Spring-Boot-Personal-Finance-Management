package org.fna.finance.service;

import org.fna.finance.exception.AccountNotFoundException;
import org.fna.finance.exception.NotEnoughFundsException;
import org.fna.finance.model.Category;
import org.fna.finance.model.User;
import org.fna.finance.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories(User user) {
        return categoryRepository.findAllByUser(user);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category getCategory(User user, Long id) throws AccountNotFoundException {
        Optional<Category> category = categoryRepository.findByUserAndId(user, id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new AccountNotFoundException(id);
        }
    }

    public Category updateCategoryName(User user, Long id, String name) throws AccountNotFoundException, NotEnoughFundsException {
        Category category = categoryRepository.findByUserAndId(user, id).orElse(null);
        if (category != null) {
            category.setName(name);
            return categoryRepository.save(category);
        } else {
            throw new AccountNotFoundException(id);
        }
    }

    public void deleteCategory(User user, Long id) throws RuntimeException {
        Optional<Category> category = categoryRepository.findByUserAndId(user, id);
        if (category.isPresent()) {
            categoryRepository.delete(category.get());
        } else {
            throw new AccountNotFoundException(id);
        }
    }


}
