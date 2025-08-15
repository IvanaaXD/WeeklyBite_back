package com.backend.weeklybite.repository;

import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository  extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
    @Query("SELECT a FROM UserAccount a WHERE a.email = :email")
    Optional<UserAccount> findByActiveEmail(@Param("email") String email);
    boolean existsByEmail(String email);
    Collection<UserAccount> findByRole(Role role);
}
