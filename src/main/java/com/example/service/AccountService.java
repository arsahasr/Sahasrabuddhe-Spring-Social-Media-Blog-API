package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) { // here we use constructor injection to inject accountRepository.
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Account> addAccount(Account account) {  // here we are getting an account object from the controller, and conducting business logic.
        if ((!account.getUsername().isBlank()) && (account.getPassword().length() >= 4)) { // logic makes sure username is not blank, and the password length is at least 4.
            List<Account> allAccounts = accountRepository.findAll();
            for (Account acct : allAccounts) {
                if (!acct.getUsername().equals(account.getUsername())) { // make sure that our account doesn't already exist in our database (is unique).
                    accountRepository.save(account); // we want to try to insert an account object into our database.
                    return ResponseEntity.status(HttpStatus.OK).body(account);   
                }
                else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(account);
                }
            }   
        }       
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(account); // this means blank username or bad password; so immediately return status 400, don't try to enter into account database.
        }

        return null;
    }

    public ResponseEntity<Account> verifyLogin(Account account) {
        List<Account> allUsers = accountRepository.findAll(); // this is a list of account objects. We want to get all the accounts from the account repository, which connects to the database.
        
        for (Account acct: allUsers) {
            if ((acct.getUsername().equals(account.getUsername()) && (acct.getPassword().equals(account.getPassword())))) { // we check that both our username and password exists in an account.
                return ResponseEntity.status(HttpStatus.OK).body(acct);
            }
        }        
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(account);   // else return 401 if if doesn't get executed.
    }
}


