package com.example.jwt.web;

import com.example.jwt.accounts.Account;
import com.example.jwt.accounts.Accounts;
import com.example.jwt.config.security.JwtAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
public class JwtController {
    private final Accounts accounts;

    public JwtController(Accounts accounts) {
        this.accounts = accounts;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<LinkedHashMap<String, String>> login(@RequestBody LinkedHashMap<String, String> credentials) {
        return ResponseEntity.ok(accounts.jwtLogin(credentials));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody LinkedHashMap<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        return ResponseEntity.ok(accounts.create(username, password).toMap());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/hello")
    public ResponseEntity<Object> hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication) {
            JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
            Account account = jwtAuthentication.getAccount();
            LinkedHashMap<String, String> response = account.toMap();
            response.put("text", "Hello, " + response.get("username"));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok("Cannot authenticate via JWT.");
        }
    }
}
