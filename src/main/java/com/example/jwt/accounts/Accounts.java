package com.example.jwt.accounts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class Accounts implements UserDetailsService {
    private final AccountsRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokensRepository tokensRepository;
    private final PasswordEncoder encoder;

    @Value("${token.expired}")
    private Integer expiredSecondsForToken;

    public Accounts(AccountsRepository repository, PasswordEncoder passwordEncoder, TokensRepository tokensRepository, PasswordEncoder encoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokensRepository = tokensRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return byUsername(username)
                .map(AccountDetails::new)
                .orElseThrow(notFound(username));
    }

    public LinkedHashMap<String, String> login(LinkedHashMap<String, String> credentials) {
        return byUsername(credentials.get("username"))
                .filter(a -> encoder.matches(credentials.get("password"), a.getPasswordHash()))
                .map(this::createTokenForAccount)
                .map(Token::toMap)
                .orElseThrow(incorrectCredentials());
    }

    public Account create(String username, String password) {
        String passwordHash = passwordEncoder.encode(password);
        Account account = new Account(username, passwordHash);
        return repository.save(account);
    }

    private Optional<Account> byUsername(String username) {
        return repository.findFirstByUsernameIgnoreCase(username);
    }

    private Token createTokenForAccount(Account account) {
        String value = UUID.randomUUID().toString();
        Token token = new Token(value, account);
        tokensRepository.save(token);
        return token;
    }

    private Supplier<BadCredentialsException> incorrectCredentials() {
        return () -> new BadCredentialsException("Incorrect login or password");
    }

    private Supplier<UsernameNotFoundException> notFound(String username) {
        return () -> new UsernameNotFoundException("No account with username " + username);
    }
}
