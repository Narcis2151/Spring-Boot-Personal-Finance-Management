package org.fna.finance.repository;

import org.fna.finance.model.Category;
import org.fna.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUser(User user);

    Optional<Category> findByUserAndId(User user, Long id);
}
