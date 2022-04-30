/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.models;


/**
 *
 * @author Syed Haseeb
 */
public class Transaction {
    private double Amount;
    private String Currency;
    private int Type;
    private String From;
    private String To;

    public Transaction() {
    }

    public double getAmount() {
        return Amount;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String From) {
        this.From = From;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String To) {
        this.To = To;
    }
    
    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }
    
    
}
