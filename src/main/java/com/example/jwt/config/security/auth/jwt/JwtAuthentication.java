package com.example.jwt.config.security.auth.jwt;

import com.example.jwt.accounts.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Collection;

public class JwtAuthentication implements Authentication {
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private String token;

    public JwtAuthentication(String jwt) {
        this.token = jwt;
    }

    public JwtAuthentication(Account account) {
        token = Jwts.builder()
                .setClaims(account.toMap())
                .signWith(key)
                .compact();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
