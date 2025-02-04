package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Constructor that injects message repository and account repository
     * @param messageRepository
     * @param accountRepository
     */
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Creates new message if messageText is not blank and over 255 characters long, and postedBy is an existing account
     * @param newMessage new message to create
     * @return new message if successful, null otherwise
     */
    public Message addNewMesesage(Message newMessage) {
        if(newMessage.getMessageText().isBlank() || newMessage.getMessageText().length() > 255) {
            return null;
        }

        Optional<Account> optionalAccount = accountRepository.findById(newMessage.getPostedBy());
        if(!optionalAccount.isPresent()) {
            return null;
        }

        return messageRepository.save(newMessage);
    }

    /**
     * Get a list of all messages
     * @return list of all messages, empty list if there are no messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Get a specific message by messageId
     * @param id messageId to query
     * @return message if it exists, null otherwise
     */
    public Message getMessageById(int id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        return optionalMessage.orElse(null);
    }

    /**
     * Delete message by messageId if message exists
     * @param id messageId to query
     */
    public void deleteMessageById(int id) {
        messageRepository.deleteById(id);
    }

    /**
     * Update message by messageId
     * @param id messageId to query
     * @param newMessageText updated message text
     * @return updated message if successful, null otherwise
     */
    public Message updateMessageById(int id, String messageText) {
        if(messageText.isBlank() || messageText.length() > 255) {
            return null;
        }

        Optional<Message> optionalMessage = messageRepository.findById(id);
        if(!optionalMessage.isPresent()) {
            return null;
        }

        Message message = optionalMessage.get();
        message.setMessageText(messageText);

        return messageRepository.save(message);
    }

    /**
     * Find all messages by accountId
     * @param id accountId to query
     * @return list of all messages by accountId, empty list if there are no messages
     */
    public List<Message> getAllMessagesByPostedBy(int id) {
        return messageRepository.findByPostedBy(id);
    }
}
