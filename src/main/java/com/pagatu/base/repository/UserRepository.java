package com.pagatu.base.repository;

import com.pagatu.base.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByAccountNumber(String accountNumber);

    boolean existsByEmail(String email);

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByAccountNumberAndIdNot(String accountNumber, Long id);
}
