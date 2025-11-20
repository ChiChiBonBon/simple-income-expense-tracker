package org.example.simpleincomeexpensetracker.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_blacklist")
@Getter
@Setter
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, columnDefinition = "LONGTEXT")
    private String token;

    @Column(nullable = false)
    private LocalDateTime blacklistedAt;

    /**
     * token的真實時間，用在清理token
     */
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = true)
    private String tokenHash;

    @Column(nullable = true)
    private Integer userId;
}
