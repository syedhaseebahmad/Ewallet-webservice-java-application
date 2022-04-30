/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.services;

import MIAGE.ewallet.models.UserLogin;
import MIAGE.ewallet.models.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import MIAGE.ewallet.repository.SignupRepository;

/**
 *
 * @author Syed Haseeb
 */
@Service
public class UserService {
    @Autowired
    private SignupRepository RPsignup;
    
    private PasswordEncoder Encoder;

    public UserService(){
        this.Encoder = new BCryptPasswordEncoder();
    }
    
    public User SignupUsers(User user){
        String EncodedPassword=this.Encoder.encode(user.getUserPass());
        user.setUserPass(EncodedPassword);
        if(!RPsignup.existsById(user.getUserEmail())){
            return RPsignup.save(user);
        }
        return null;
    }
    public User LoginUsers(UserLogin user){
        User LoginUser = new User();
        LoginUser.setUserEmail(user.getUserEmail());
        LoginUser.setUserPass(user.getUserPassword());
        Optional<User> CheckedUser=RPsignup.findById(LoginUser.getUserEmail());
        if(CheckedUser.isPresent()){
            User DBuser=CheckedUser.get();
            if(Encoder.matches(user.getUserPassword(),DBuser.getUserPass())){
                return DBuser;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
}
