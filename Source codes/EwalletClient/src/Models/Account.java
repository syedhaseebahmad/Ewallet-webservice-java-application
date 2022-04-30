/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Sanjeewa Kulathunga
 */
public class Account {
    private String UserEmail;
    private double Balance;
    private String Currency;
    private double AltBalance;

    public Account() {
    }

    public double getAltBalance() {
        return AltBalance;
    }

    public void setAltBalance(double AltBalance) {
        this.AltBalance = AltBalance;
    }
    
    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String UserEmail) {
        this.UserEmail = UserEmail;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double Balance) {
        this.Balance = Balance;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }
    
    
}
