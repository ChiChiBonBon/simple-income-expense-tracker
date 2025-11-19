package org.example.simpleincomeexpensetracker.service.impl;

import org.example.simpleincomeexpensetracker.entity.Users;
import org.example.simpleincomeexpensetracker.repository.UsersRepository;
import org.example.simpleincomeexpensetracker.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 使用者服務實作
 */
@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private static final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Users findByUsername(String username) {
        log.info("查詢使用者: {}", username);
        return usersRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return usersRepository.existsByUsername(username);
    }

    @Override
    public Users save(Users user) {
        log.info("儲存使用者: {}", user.getUsername());
        user.setCreatedAt(LocalDateTime.now());
        return usersRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Users authenticate(String username, String password) {
        log.info("驗證使用者: {}", username);
        Users users = usersRepository.findByUsername(username);

        if (users == null) {
            log.warn("使用者不存在: {}", username);
            return null;
        }

        // 驗證密碼
        if (passwordEncoder.matches(password, users.getPassword())) {
            log.info("使用者驗證成功: {}", username);
            return users;
        } else {
            log.warn("密碼錯誤: {}", username);
            return null;
        }
    }
}