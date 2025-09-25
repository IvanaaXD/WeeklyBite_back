package com.backend.weeklybite.repository;

import com.backend.weeklybite.domain.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {

    Optional<ActivationToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM ActivationToken t WHERE t.expiryDate <= :date")
    void deleteAllExpiredSince(@Param("date") LocalDateTime date);
}
