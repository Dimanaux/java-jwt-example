package com.example.jwt.config.security.auth.token;

import com.example.jwt.accounts.AccountDetails;
import com.example.jwt.accounts.Accounts;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {
    private final Accounts accounts;

    public TokenAuthenticationProvider(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // делаем явное преобразование для работы с TokenAuthentication
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        // загружаем данные безопасности пользователя из UserDetailsService
        // по токену достали пользователя из БД
        AccountDetails accountDetails = (AccountDetails) accounts.loadUserByUsername(tokenAuthentication.getName());
        // если данные пришли
        if (accountDetails != null && accountDetails.getCurrentToken().isNotExpired()) {
            // в данный объект аутентификации кладем пользователя
            tokenAuthentication.setUserDetails(accountDetails);
            // говорим, что с ним все окей
            tokenAuthentication.setAuthenticated(true);
        } else {
            throw new BadCredentialsException("Incorrect Token");
        }
        // возвращаем объект SecurityContext-у
        return tokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthentication.class.equals(authentication);
    }
}
