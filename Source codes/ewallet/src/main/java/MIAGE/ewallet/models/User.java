/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Sanjeewa Kulathunga
 */
@Entity
@Table(name="User")
public class User {
    @Id
    private String UserEmail;
    private String UserNic;
    private String UserPass;
    private String UserFname;
    private String UserLname;
    private String UserDob;
    private String UserGender;
    private String UserMstatus;
    private String UserAddress;
    private String UserCity;  

    public User() {
    }
    
    public String getUserNic() {
        return UserNic;
    }

    public void setUserNic(String UserNic) {
        this.UserNic = UserNic;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String UserEmail) {
        this.UserEmail = UserEmail;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String UserPass) {
        this.UserPass = UserPass;
    }

    public String getUserFname() {
        return UserFname;
    }

    public void setUserFname(String UserFname) {
        this.UserFname = UserFname;
    }

    public String getUserLname() {
        return UserLname;
    }

    public void setUserLname(String UserLname) {
        this.UserLname = UserLname;
    }

    public String getUserDob() {
        return UserDob;
    }

    public void setUserDob(String UserDob) {
        this.UserDob = UserDob;
    }

    public String getUserGender() {
        return UserGender;
    }

    public void setUserGender(String UserGender) {
        this.UserGender = UserGender;
    }

    public String getUserMstatus() {
        return UserMstatus;
    }

    public void setUserMstatus(String UserMstatus) {
        this.UserMstatus = UserMstatus;
    }

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String UserAddress) {
        this.UserAddress = UserAddress;
    }

    public String getUserCity() {
        return UserCity;
    }

    public void setUserCity(String UserCity) {
        this.UserCity = UserCity;
    }

   
    
    
}
