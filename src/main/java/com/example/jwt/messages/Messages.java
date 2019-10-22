package com.example.jwt.messages;

import com.example.jwt.accounts.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Messages {
    private final MessageRepository repository;
    private Message lastMessage;

    public Messages(MessageRepository repository) {
        this.repository = repository;
    }

    synchronized
    public Message waitForNewMessage() {
        safeWait();
        return lastMessage;
    }

    synchronized
    public Message create(Account author, String text) {
        Message message = new Message(author, text);
        lastMessage = repository.save(message);
        notifyAll();
        return lastMessage;
    }

    public List<Message> history() {
        return repository.findAll();
    }

    private void safeWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            System.err.println("There was 1 client waiting for messages.");
        }
    }
}
