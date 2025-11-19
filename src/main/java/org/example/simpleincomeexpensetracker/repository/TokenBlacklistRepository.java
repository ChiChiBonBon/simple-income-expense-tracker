package org.example.simpleincomeexpensetracker.repository;

import org.example.simpleincomeexpensetracker.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Integer> {

    /**
     * 依據hash值查詢是否在黑名單中
     */
    boolean existsByTokenHash(String tokenHash);

    /**
     * 删除過期的黑名單記錄
     */
    void deleteByBlacklistedAtBefore(java.time.LocalDateTime dateTime);
}