package com.example.jwt.config.security.auth.token;

import com.example.jwt.accounts.AccountDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthentication implements Authentication {
    private AccountDetails accountDetails;
    private String token;
    private boolean isAuthenticated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return accountDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return accountDetails.getPassword();
    }

    @Override
    public Object getDetails() {
        return accountDetails;
    }

    @Override
    public Object getPrincipal() {
        return accountDetails.getAccount();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return token;
    }

    public void setUserDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public void setToken(String tokenValue) {
        this.token = tokenValue;
    }
}
