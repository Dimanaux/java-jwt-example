package com.example.jwt.web;

import com.example.jwt.accounts.Accounts;
import com.example.jwt.accounts.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
public class LoginController {

    @Autowired
    private Accounts accounts;

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Token> login(@RequestBody LinkedHashMap<String, String> credentials) {
        return ResponseEntity.ok(accounts.login(credentials));
    }
}
