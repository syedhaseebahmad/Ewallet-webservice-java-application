/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.controllers;

import MIAGE.ewallet.models.UserLogin;
import MIAGE.ewallet.models.User;
import MIAGE.ewallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Syed Haseeb
 */
@RestController
@RequestMapping("/api/v1/")
public class UserController {
    
    @Autowired
    private UserService userservice;

    public UserController() {
        userservice= new UserService();
    }

    @RequestMapping(value="/signup",consumes = "application/json", produces = "application/json", method=RequestMethod.POST)
    public ResponseEntity<User> Signup(@RequestBody User newSignupUser) {
        try{
            User newSignup=userservice.SignupUsers(newSignupUser);
            if(newSignup!=null){
                return new ResponseEntity<User>(newSignup,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }
    
    @RequestMapping(value="/login",consumes = "application/json", produces = "application/json", method=RequestMethod.POST)
    public ResponseEntity<User> Login(@RequestBody UserLogin newLoginUser) {
        try{
            User user=userservice.LoginUsers(newLoginUser);
            if(user!=null){
                return new ResponseEntity<User>(user,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }
    
}
