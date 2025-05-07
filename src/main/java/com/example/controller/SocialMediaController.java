package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List; // Imports the List interface

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
// @Controller not needed because we are using @RestController that incorporates both Controller and ResponseBody.
// @ResponseBody indicates that the return values of the methods in the class should be directly serialized to the HTTP body. Return values should not be interpreted as a view name but rather as the response content itself.
// we don't need base url, as there is no constant url. It is always based on our user request.
@RestController // this is a specialized version of the @Controller that also incorporates @ResponseBody. Uses @ResponseBody to all of its methods by default. 
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(AccountService as, MessageService ms) { // here, we are injecting ms and as into our SocialMediaController. This is using a form of injection called constructor injection.
        this.accountService = as;
        this.messageService = ms;
    }

    @PostMapping("/register") 
    public ResponseEntity<Account> register(@RequestBody Account account) { // our method will return an object of type account, and have an account object as an argument.
        
        ResponseEntity<Account> newacc_response = accountService.addAccount(account); // whatever this returns, we will assign it to newacc. It is of type Response Entity (account).
        return newacc_response; // we will return a variable of ResponseEntity account type. 
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {  // get an account object. We use @RequestBody, mapping the Http request body to a Java object.

        ResponseEntity<Account> loginacc_response = accountService.verifyLogin(account);
        return loginacc_response;   
    }

    @PostMapping("/messages") // we want to post a message to the database
    public ResponseEntity<Message> newmessage(@RequestBody Message message) { // extract msg
        ResponseEntity<Message> messageacc_response = messageService.addMessage(message);
        return messageacc_response;

    }
    
    @GetMapping("/messages")
    public List<Message> getAllMsgs() { // we want to receive all messages in a list.
        return messageService.getAllMessages(); // return that list to our client.
    }

    @GetMapping("/messages/{messageid}") 
    public ResponseEntity<Message> getMsgById(@PathVariable Integer messageid) { // extract our messageid through @pathvariable as an int
        ResponseEntity<Message> msg_by_id_response = messageService.getMessageByID(messageid);
        return msg_by_id_response; // return our response entity, received from our messageservice.
    }
    
    @DeleteMapping("/messages/{messageid}")
    public ResponseEntity<Integer> delMsgById(@PathVariable Integer messageid) { // extract our messageid through @pathvariable as an int
        ResponseEntity<Integer> msg_by_id_response = messageService.deleteMessageByID(messageid);
        return msg_by_id_response; // return our response entity, received from our messageservice.
    }

    @PatchMapping("/messages/{messageid}")
    public ResponseEntity<Integer> updateMsgById(@RequestBody Message message_text, @PathVariable Integer messageid) { // extract our message_text through @Requestbody, and messageid through @pathvariable as an int
        ResponseEntity<Integer> msg_by_id_response = messageService.updateMessageByID(message_text, messageid);
        return msg_by_id_response; // return our response entity, received from our messageservice.
    }

    @GetMapping("/accounts/{account_id}/messages") // extract our accountid through @pathvariable as an int
    public List<Message> getMsgsByID(@PathVariable Integer account_id) {
        return messageService.getAllMessagesByID(account_id); // receive all messages by an account id through our messageservice.
    }
}
