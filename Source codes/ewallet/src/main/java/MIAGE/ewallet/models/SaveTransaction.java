/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Sanjeewa Kulathunga
 */
@Entity
@Table(name="Transactions")
public class SaveTransaction {
    @Id
    @GeneratedValue
    private int ID;
    private String UserEmail;
    private double Amount;
    private String DateTime;
    private String Currency;
    private String Type;

    public SaveTransaction() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String DateTime) {
        this.DateTime = DateTime;
    }
    
    
    public double getAmount() {
        return Amount;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String UserEmail) {
        this.UserEmail = UserEmail;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public String getDateandTime() {
        return DateTime;
    }

    public void setDateandTime(String DateandTime) {
        this.DateTime = DateandTime;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
    
    
}
