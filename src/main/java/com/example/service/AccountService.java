package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUserException;
import com.example.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Constructor that injects accountRepository
     * @param accountRepository
     */
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates new account if newAccount username is not blank, the password is at least 4 characters long, and there is no 
     * other account with the same username
     * @param newAccount new account to be added
     * @return new account if successful
     * @throws DuplicateUserException if there exists a user with the same username
     */
    public Account addNewAccount(Account newAccount) throws DuplicateUserException {
        if(newAccount.getUsername().isBlank() || newAccount.getPassword().length() < 4) {
            throw new IllegalArgumentException();
        }

        List<Account> accounts = accountRepository.findAll();
        for(Account a : accounts) {
            if(a.getUsername().equals(newAccount.getUsername())) {
                throw new DuplicateUserException();
            }
        }

        return accountRepository.save(newAccount);
    }

    /**
     * Login is successful if there exists an account with the matching username and password
     * @param username username to query
     * @param password password to query
     * @return account if it exists, null otherwise
     */
    public Account login(Account account) {
        Optional<Account> optionalAccount = 
            accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        return optionalAccount.orElse(null);
    }
}
