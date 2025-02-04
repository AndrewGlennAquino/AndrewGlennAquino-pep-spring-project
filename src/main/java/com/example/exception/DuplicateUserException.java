package com.example.exception;

public class DuplicateUserException extends Exception {
    
    // No-args constructor
    public DuplicateUserException() {
    }

    // Constructor with message
    public DuplicateUserException(String message) {
        super(message);
    }
}