package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUserException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private MessageService messageService;

    /**
     * Register a new account if and only if the username is not blank, the password is at least 4 characters long,
     * and there is no other account with the same username
     * @param newAccount new account to register
     * @return new account including accountId if successful
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Account newAccount) {
        Account account;

        try {
            account = accountService.addNewAccount(newAccount);
        } catch(DuplicateUserException e) {
            return ResponseEntity.status(409).body(null);
        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.status(200).body(account);
    }

    /**
     * Login into an existing account
     * @param account account to login
     * @return existing account including accountId if successful
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Account account) {
        Account user = accountService.login(account);
        
        if(user == null) {
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.status(200).body(user);
    }

    /**
     * Creates a new message if and only if messageText is not blank, is not over 255 characters, and psotedBy
     * refers to a real, existing user
     * @param newMessage message to create
     * @return new message with messageId if successful
     */
    @PostMapping("/messages")
    public ResponseEntity createMessage(@RequestBody Message newMessage) {
        Message message = messageService.addNewMesesage(newMessage);

        if(message == null) {
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.status(200).body(message);
    }

    /**
     * Get all messages
     * @return list of all messages, empty list if there are no messages
     */
    @GetMapping("/messages")
    public ResponseEntity getAllMessages() {
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    /**
     * Retrieve a specific message using messageId
     * @param messageId message to query
     * @return message if it exists, empty if it does not
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity getMessageById(@PathVariable int messageId) {
        return ResponseEntity.status(200).body(messageService.getMessageById(messageId));
    }

    /**
     * Delete a message if it exists
     * @param messageId message to query
     * @return number of rows affected
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity deleteMessageById(@PathVariable int messageId) {
        if(messageService.getMessageById(messageId) != null) {
            messageService.deleteMessageById(messageId);
            return ResponseEntity.status(200).body(1);
        }
        
        return ResponseEntity.status(200).body(null);
    }

    /**
     * Update message using messageId if newMessage is not blank and less than 255 characters
     * @param messageId message to update
     * @param newMessage new message
     * @return number of rows affected
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity updateMessageById(@PathVariable int messageId, @RequestBody Message message) {
        Message updatedMessage = messageService.updateMessageById(messageId, message.getMessageText());

        if(updatedMessage == null) {
            return ResponseEntity.status(400).body(null);
        } else {
            return ResponseEntity.status(200).body(1);
        }
    }

    /**
     * Get all messages from a specific account
     * @param accountId account to get all messages from
     * @return list of all messages from accountId, empty list if there are no messages
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getAllMessageByAccount(@PathVariable int accountId) {
        return ResponseEntity.status(200).body(messageService.getAllMessagesByPostedBy(accountId));
    }
}
