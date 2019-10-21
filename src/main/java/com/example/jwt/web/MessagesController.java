package com.example.jwt.web;

import com.example.chats.accounts.Account;
import com.example.chats.accounts.AccountDetails;
import com.example.chats.messages.Message;
import com.example.chats.messages.Messages;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/messages")
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

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Message> createNewMessage(
            @RequestBody LinkedHashMap<String, Object> body,
            @AuthenticationPrincipal Authentication authentication) {
        Account author = ((AccountDetails) authentication.getPrincipal()).getAccount();
        Message message = messages.create(author, (String) body.get("text"));
        return ResponseEntity.ok(message);
    }
}
