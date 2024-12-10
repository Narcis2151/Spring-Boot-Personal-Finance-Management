package org.fna.finance.repository;

import org.fna.finance.model.Account;
import org.fna.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    public List<Account> findAllByUser(User user);

    public Optional<Account> findByUserAndId(User user, Long id);
}
