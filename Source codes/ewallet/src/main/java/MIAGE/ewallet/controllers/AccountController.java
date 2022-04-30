/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.controllers;

import MIAGE.ewallet.models.Account;
import MIAGE.ewallet.models.SaveTransaction;
import MIAGE.ewallet.models.Transaction;
import MIAGE.ewallet.services.AccountService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sanjeewa Kulathunga
 */
@RestController
@RequestMapping("/api/v1/")
public class AccountController {
    @Autowired
    private AccountService accservice;

    public AccountController() {
        accservice= new AccountService();
    }
    
    @RequestMapping(value="/accountbyid/{UserEmail}", method=RequestMethod.GET)
    public ResponseEntity<Account> CheckBalance(@PathVariable String UserEmail) {
        try{
            Account account=accservice.checkBalance(UserEmail);
            if(account!=null){
                return new ResponseEntity<Account>(account,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }
    
    @RequestMapping(value="/createaccount",consumes = "application/json", produces = "application/json", method=RequestMethod.POST)
    public ResponseEntity<Account> CreateAccount(@RequestBody Account newAccount) {
        try{
            Account CreatedAccount=accservice.CreateAccount(newAccount);
            if(CreatedAccount!=null){
                return new ResponseEntity<Account>(CreatedAccount,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }
    
    @RequestMapping(value="/deposit",consumes = "application/json", produces = "application/json", method=RequestMethod.POST)
    public ResponseEntity<Transaction> Deposit(@RequestBody Transaction newTransaction) {
        try{
            Transaction CreatedTransaction=accservice.Deposit(newTransaction);
            if(CreatedTransaction!=null){
                return new ResponseEntity<Transaction>(CreatedTransaction,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }
    
    @RequestMapping(value="/withdraw",consumes = "application/json", produces = "application/json", method=RequestMethod.POST)
    public ResponseEntity<Transaction> Withdraw(@RequestBody Transaction newTransaction) {
        try{
            Transaction CreatedTransaction=accservice.Withdraw(newTransaction);
            if(CreatedTransaction!=null){
                return new ResponseEntity<Transaction>(CreatedTransaction,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }
    
    @RequestMapping(value="/paybill",consumes = "application/json", produces = "application/json", method=RequestMethod.POST)
    public ResponseEntity<Transaction> BillPayment(@RequestBody Transaction newTransaction) {
        try{
            Transaction CreatedTransaction=accservice.PayBill(newTransaction);
            if(CreatedTransaction!=null){
                return new ResponseEntity<Transaction>(CreatedTransaction,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }
    
    @RequestMapping(value="/transfer",consumes = "application/json", produces = "application/json", method=RequestMethod.POST)
    public ResponseEntity<Transaction> Tranfer(@RequestBody Transaction newTransaction) {
        try{
            Transaction CreatedTransaction=accservice.TranfertoAnother(newTransaction);
            if(CreatedTransaction!=null){
                return new ResponseEntity<Transaction>(CreatedTransaction,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }
    
    @RequestMapping(value="/deleteaccount/{UserEmail}", method=RequestMethod.DELETE)
    public ResponseEntity<String> DeleteAccount(@PathVariable String UserEmail) {
        try{
            boolean isDeleted=accservice.DeleteAccount(UserEmail);
            if(isDeleted==true){
                return new ResponseEntity<String>(UserEmail,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }
}