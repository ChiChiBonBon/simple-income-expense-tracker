package org.example.simpleincomeexpensetracker.repository;

import org.example.simpleincomeexpensetracker.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 使用者資料存取層
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    /**
     * 根據使用者名稱查詢使用者
     */
    Users findByUsername(String username);

    /**
     * 檢查使用者名稱是否存在
     */
    boolean existsByUsername(String username);
}