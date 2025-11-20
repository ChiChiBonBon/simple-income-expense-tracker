package org.example.simpleincomeexpensetracker.repository;

import org.example.simpleincomeexpensetracker.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Integer> {
    boolean existsByToken(String token);
    long deleteByExpiresAtBefore(LocalDateTime dateTime);
}