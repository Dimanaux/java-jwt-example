package com.example.jwt.web;

import com.example.jwt.accounts.Account;
import com.example.jwt.accounts.AccountDetails;
import com.example.jwt.config.security.JwtAuthentication;
import com.example.jwt.messages.Message;
import com.example.jwt.messages.Messages;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "http://localhost:3000")
public class MessagesController {
    private final Messages messages;

    public MessagesController(Messages messages) {
        this.messages = messages;
    }

    @GetMapping("/notification")
    public ResponseEntity<Message> notifyAboutNewMessages() {
        Message message = messages.waitForNewMessage();
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<Message>> messageHistory() {
        return ResponseEntity.ok(messages.history());
    }

    @PostMapping
    public ResponseEntity<Message> createNewMessage(@RequestBody LinkedHashMap<String, Object> body) {
        Account author = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getAccount();
        Message message = messages.create(author, (String) body.get("text"));
        return ResponseEntity.ok(message);
    }
}
