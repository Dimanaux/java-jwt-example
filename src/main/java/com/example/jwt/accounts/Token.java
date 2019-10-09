package com.example.jwt.accounts;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@Entity
public class Token {
    @Value("${token.expired}")
    private Integer expiredSecondsForToken;

    @Id
    @GeneratedValue
    private Long id;
    private String value;

    @ManyToOne
    @JoinColumn
    private Account account;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public Token() {
        createdAt = LocalDateTime.now();
        expiresAt = createdAt.plusSeconds(expiredSecondsForToken);
    }

    public Token(String value, Account account) {
        this();
        this.account = account;
        this.value = value;
    }

    public LinkedHashMap<String, String> toMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("value", value);
        return map;
    }

    public boolean isNotExpired() {
        return LocalDateTime.now().isBefore(expiresAt);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
