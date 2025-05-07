package com.example.service;

import org.aspectj.internal.lang.annotation.ajcDeclareParents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List; // Imports the List interface
import java.util.Optional;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;



@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) { // here we use constructor injection to inject messagerepository and accountRepository.
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository; // we also initialize and inject accountrepo, because we will need to access the the acctrepo when trying to add a message (acct_id needed).
    }

    public ResponseEntity<Message> addMessage(Message message) { // add a message to the database.
        if ((!message.getMessageText().isBlank()) && (message.getMessageText().length() <= 255)) { // check msg text length and whether it is blank first.
            List<Account> allAccts = accountRepository.findAll(); // Access all accts because we need to see if an account_id exists.
            for (Account acct : allAccts) {
                if (acct.getAccountId().equals(message.getPostedBy())) { // if one account object id equals our message.getpostedby:
                    Message retMsg = messageRepository.save(message); // persist to database
                    return ResponseEntity.status(HttpStatus.OK).body(retMsg);  // this body(message) doesn't have a messageid. So how do the tests pass?
                }
            }
        }     
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message); // if first two conditions were not met, give a 400.
    }
    

    public List<Message> getAllMessages() {
        return messageRepository.findAll(); // findAll gives us all messages as a list.
    }

    public ResponseEntity<Message> getMessageByID(Integer messageid) {
        Optional<Message> retVal = messageRepository.findById(messageid); // tries to find a message entity 
        if (retVal.isPresent()) { // checks if the entity with that id exists, or is present.
            Message msg = retVal.get(); // we get our message entity from retval, and set it equal to a variable of message type.
            return ResponseEntity.status(HttpStatus.OK).body(msg); // we return msg as a response entity object.
        }

        else {
            return null;  // if there is no such message, it is expected that the message body is simply empty. There is nothing to get.
        }

    }

    public ResponseEntity<Integer> deleteMessageByID(Integer messageid) { // we want to delete a message based on its ID.
        Optional<Message> retVal = messageRepository.findById(messageid);
        if (retVal.isPresent()) { // checks if the entity with that id exists, or is present.
            messageRepository.deleteById(messageid); // here, we delete a message by the id provided from our database.
            return ResponseEntity.status(HttpStatus.OK).body(1); // return an ok message, with a value of 1 (the number of rows updated).
        }
        else {
            return null; // if there is no such message, it is expected that the message body is simply empty. There is nothing to delete.
        }
    }

    public ResponseEntity<Integer> updateMessageByID(Message message_text, Integer messageid) { // we want to update a message by its id. We have msg_text, and msg_id as our parameters. 

        if ((!message_text.getMessageText().isBlank()) && (message_text.getMessageText().length() <= 255)) { // here we are checking if the message_text (which we received from the client/user) satisfies these two conditions first.
            Optional<Message> retVal = messageRepository.findById(messageid); // this returns an entity with the given messageid.
            
            if (retVal.isPresent()) { // check if this value is present in the repository, identifiable by its id.
                Message message = retVal.get();
                message.setMessageText(message_text.getMessageText());
                messageRepository.save(message); // save this updated message in our repository.

                return ResponseEntity.status(HttpStatus.OK).body(1); // return updated rows (1) and 200 response.
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // else return 400 error.
    }

    public List<Message> getAllMessagesByID(Integer accountid) { // retrieve our accountid from our controller.
        List<Message> allMsgs = messageRepository.findAll(); // get all the messages in our database as a list of messages. each item is a message.
        List<Message> retList = new ArrayList<>(); // create a new empty list.
        for (Message msg : allMsgs) { // go through each message object in our all messages list.
            if (accountid.equals(msg.getPostedBy())) { // if our account id, which references a specific message equals message.getposted by. 
                retList.add(msg); // then, add this message to our empty list. We want retlist to contain all the messages for a specific accountid.
            }
        }
        return retList; // finally we return this list of messages.
    }
}


