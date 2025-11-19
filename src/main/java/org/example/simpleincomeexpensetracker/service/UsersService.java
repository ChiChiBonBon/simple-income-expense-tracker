package org.example.simpleincomeexpensetracker.service;

import org.example.simpleincomeexpensetracker.entity.Users;

/**
 * 使用者服務介面
 */
public interface UsersService {

    /**
     * 根據使用者名稱查詢使用者
     */
    Users findByUsername(String username);

    /**
     * 檢查使用者名稱是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 儲存使用者
     */
    Users save(Users user);

    /**
     * 驗證使用者（帳號密碼）
     */
    Users authenticate(String username, String password);
}