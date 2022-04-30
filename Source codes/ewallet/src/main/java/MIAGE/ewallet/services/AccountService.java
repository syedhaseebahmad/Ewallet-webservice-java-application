/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.services;

import MIAGE.ewallet.External.Convert;
import MIAGE.ewallet.External.ExternalAPI;
import MIAGE.ewallet.models.Account;
import MIAGE.ewallet.models.SaveTransaction;
import MIAGE.ewallet.models.Transaction;
import MIAGE.ewallet.models.User;
import MIAGE.ewallet.repository.AccountRepository;
import MIAGE.ewallet.repository.SignupRepository;
import MIAGE.ewallet.repository.TransactionRepository;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sanjeewa Kulathunga
 */
@Service
public class AccountService {
    @Autowired
    private AccountRepository  RPaccount;
    @Autowired
    private TransactionRepository  RPtransaction;
    @Autowired
    private SignupRepository RPsignup;

    public AccountService() {
        
    }
    
    public Account CreateAccount(Account account){
        if(RPsignup.existsById(account.getUserEmail())){
            if(!RPaccount.existsById(account.getUserEmail())){
                RPaccount.save(account);
                setAlternativeBalance(account.getUserEmail());
                return account;
            }
            return null;
        }
        return null;
    }
    public Boolean DeleteAccount(String UserEmail){
        if(RPsignup.existsById(UserEmail)){
           RPsignup.deleteById(UserEmail);
           RPaccount.deleteById(UserEmail);
           return true;
        }
        return false;
    }
    public Account checkBalance(String UserEmail){
        if(UserEmail!=null){
            Optional accountDB =RPaccount.findById(UserEmail);
            return (Account) accountDB.get();
        }
        return null;
    }
    public void setAlternativeBalance(String UserEmail){
        if(UserEmail!=null){
            Optional accountDB =RPaccount.findById(UserEmail);
            Account UserAcc=(Account)accountDB.get();
            double AccBalance=UserAcc.getBalance();
            String AccCurrency=UserAcc.getCurrency();
            double ConversionRate=0;
            ExternalAPI API=new Convert();
            if(AccCurrency.equals("EUR")){
                ConversionRate=API.getEURUSD();
            }
            if(AccCurrency.equals("USD")){
                ConversionRate=API.getUSDEUR();
            }
            Double AltAccBalance=AccBalance*ConversionRate;
            DecimalFormat df =new DecimalFormat("#.##");
            AltAccBalance=Double.valueOf(df.format(AltAccBalance));
            UserAcc.setAltBalance(AltAccBalance);
            RPaccount.save(UserAcc);
        }
    }
    
    public Transaction Deposit(Transaction transaction){  
        if(transaction.getType()==1){
            if(transaction.getAmount()>0){
                Optional<Account> SearchedAccountDB =RPaccount.findById(transaction.getTo());
                Account ToAccount=SearchedAccountDB.get();
                Optional<User> AccountOwnerDB =RPsignup.findById(transaction.getTo());
                User AccountOwner=AccountOwnerDB.get();
                double CrAccountBalance=0;
                double TransactionBalance=0;
                if(transaction.getCurrency().equals(ToAccount.getCurrency())){
                    CrAccountBalance=ToAccount.getBalance();
                    TransactionBalance=transaction.getAmount();
                }else{
                    try{
                        ExternalAPI API=new Convert();
                        String AccountCurrency=ToAccount.getCurrency();
                        double ConversionRate=0.0;
                        if(AccountCurrency.equals("EUR")){
                            ConversionRate=API.getUSDEUR();
                        }else if(AccountCurrency.equals("USD")){
                           ConversionRate=API.getEURUSD();
                        }else{
                            return null;
                        }
                        if(ConversionRate>0){
                            CrAccountBalance=ToAccount.getBalance();
                            TransactionBalance=transaction.getAmount()*ConversionRate;
                       }
                    }catch(Exception e){
                        return null;
                    }
                }
                if(TransactionBalance>0){
                    double newAccountBalance=CrAccountBalance+TransactionBalance;
                    DecimalFormat df =new DecimalFormat("#.##");
                    newAccountBalance=Double.valueOf(df.format(newAccountBalance));
                    TransactionBalance=Double.valueOf(df.format(TransactionBalance));
                    ToAccount.setBalance(newAccountBalance);
                    RPaccount.save(ToAccount);
                    setAlternativeBalance(ToAccount.getUserEmail());
                    SaveTransaction newTransaction = new SaveTransaction();
                    newTransaction.setAmount(TransactionBalance);
                    newTransaction.setUserEmail(AccountOwner.getUserEmail());
                    LocalDateTime myDateObj = LocalDateTime.now();
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    newTransaction.setDateandTime(myDateObj.format(myFormatObj));
                    newTransaction.setType("Deposit");
                    newTransaction.setCurrency(ToAccount.getCurrency());
                    RPtransaction.save(newTransaction);
                    return transaction;
                }else{
                     return null;
                }
            }
        }else{
            return null;
        }  
        return null;
    }
    public Transaction Withdraw(Transaction transaction){
        if(transaction.getType()==2){
            if(transaction.getAmount()>0){
                Optional<Account> FromAccountDB =RPaccount.findById(transaction.getFrom());
                Account FromAccount=FromAccountDB.get();
                Optional<User> AccountOwnerDB =RPsignup.findById(transaction.getFrom());
                User AccountOwner=AccountOwnerDB.get();
                double CrAccountBalance=0;
                double TransactionBalance=0;
                if(transaction.getCurrency().equals(FromAccount.getCurrency())){
                    CrAccountBalance=FromAccount.getBalance();
                    TransactionBalance=transaction.getAmount();
                }else{
                    try{
                        ExternalAPI API=new Convert();
                        String AccountCurrency=FromAccount.getCurrency();
                        double ConversionRate=0;
                        if(AccountCurrency.equals("EUR")){
                            ConversionRate=API.getUSDEUR();
                        }else if(AccountCurrency.equals("USD")){
                           ConversionRate=API.getEURUSD();
                        }else{
                            return null;
                        }
                        if(ConversionRate>0){
                            CrAccountBalance=FromAccount.getBalance();
                            TransactionBalance=transaction.getAmount()*ConversionRate;
                       }
                    }catch(Exception e){
                        return null;
                    }
                }
                if(CrAccountBalance>0 && TransactionBalance>0){
                    if(CrAccountBalance>TransactionBalance){
                        double newAccountBalance=CrAccountBalance-TransactionBalance;
                        DecimalFormat df =new DecimalFormat("#.##");
                        newAccountBalance=Double.valueOf(df.format(newAccountBalance));
                        TransactionBalance=Double.valueOf(df.format(TransactionBalance));
                        FromAccount.setBalance(newAccountBalance);
                        RPaccount.save(FromAccount);
                        setAlternativeBalance(FromAccount.getUserEmail());
                        SaveTransaction newTransaction = new SaveTransaction();
                        newTransaction.setAmount(TransactionBalance);
                        newTransaction.setUserEmail(AccountOwner.getUserEmail());
                        LocalDateTime myDateObj = LocalDateTime.now();
                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        newTransaction.setDateandTime(myDateObj.format(myFormatObj));
                        newTransaction.setType("Withdraw");
                        newTransaction.setCurrency(FromAccount.getCurrency());
                        RPtransaction.save(newTransaction);
                        return transaction;
                    }else{
                        return null;
                    }
                }else{
                     return null;
                }
            }
        }else{
            return null;
        }  
        return null;
    }
    public Transaction PayBill(Transaction transaction){
        if(transaction.getType()==3){
            if(transaction.getAmount()>0){
                Optional<Account> FromAccountDB =RPaccount.findById(transaction.getFrom());
                Account FromAccount =FromAccountDB.get();
                Optional<User> AccountOwnerDB =RPsignup.findById(transaction.getFrom());
                User AccountOwner=AccountOwnerDB.get();
                double CrAccountBalance=0;
                double TransactionBalance=0;
                if(transaction.getCurrency().equals(FromAccount.getCurrency())){
                    CrAccountBalance=FromAccount.getBalance();
                    TransactionBalance=transaction.getAmount();
                }else{
                    try{
                        ExternalAPI API=new Convert();
                        String AccountCurrency=FromAccount.getCurrency();
                        double ConversionRate=0;
                        if(AccountCurrency.equals("EUR")){
                            ConversionRate=API.getUSDEUR();
                        }else if(AccountCurrency.equals("USD")){
                           ConversionRate=API.getEURUSD();
                        }else{
                            return null;
                        }
                        if(ConversionRate>0){
                            CrAccountBalance=FromAccount.getBalance();
                            TransactionBalance=transaction.getAmount()*ConversionRate;
                       }
                    }catch(Exception e){
                        return null;
                    }
                }
                if(CrAccountBalance>0 && TransactionBalance>0){
                    if(CrAccountBalance>TransactionBalance){
                        double newAccountBalance=CrAccountBalance-TransactionBalance;
                        DecimalFormat df =new DecimalFormat("#.##");
                        newAccountBalance=Double.valueOf(df.format(newAccountBalance));
                        TransactionBalance=Double.valueOf(df.format(TransactionBalance));
                        FromAccount.setBalance(newAccountBalance);
                        RPaccount.save(FromAccount);
                        setAlternativeBalance(FromAccount.getUserEmail());
                        SaveTransaction newTransaction = new SaveTransaction();
                        newTransaction.setAmount(TransactionBalance);
                        newTransaction.setUserEmail(AccountOwner.getUserEmail());
                        LocalDateTime myDateObj = LocalDateTime.now();
                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        newTransaction.setDateandTime(myDateObj.format(myFormatObj));
                        newTransaction.setType("Bill Payment");
                        newTransaction.setCurrency(FromAccount.getCurrency());
                        RPtransaction.save(newTransaction);
                        return transaction;
                    }else{
                        return null;
                    }
                }else{
                     return null;
                }
            }
        }
        return null;
    }
    
    public Transaction TranfertoAnother(Transaction transaction){
        if(transaction.getType()==4){
            if(transaction.getAmount()>0){
                Optional<Account> FromAccountDB =RPaccount.findById(transaction.getFrom());
                Account FromAccount =FromAccountDB.get();
                Optional<Account> ToAccountDB =RPaccount.findById(transaction.getTo());
                Account ToAccount =ToAccountDB.get();
                Optional<User> AccountOwnerDB =RPsignup.findById(transaction.getFrom());
                User AccountOwner=AccountOwnerDB.get();
                double CrAccountBalance=0;
                double TransactionBalance=0;
                transaction.setCurrency(FromAccount.getCurrency());
                if(FromAccount.getCurrency().equals(ToAccount.getCurrency())){
                    CrAccountBalance=FromAccount.getBalance();
                    TransactionBalance=transaction.getAmount();
                }else{
                    try{
                        ExternalAPI API=new Convert();
                        String FromAccountCurrency=FromAccount.getCurrency();
                        String ToAccountCurrency=ToAccount.getCurrency();
                        double ConversionRate=0;
                        if(FromAccountCurrency.equals("EUR") && ToAccountCurrency.equals("USD")){
                            ConversionRate=API.getEURUSD();
                        }else if(FromAccountCurrency.equals("USD") && ToAccountCurrency.equals("EUR")){
                            ConversionRate=API.getUSDEUR();
                        }else{
                            return null;
                        }
                        if(ConversionRate>0){
                            CrAccountBalance=FromAccount.getBalance();
                            TransactionBalance=transaction.getAmount()*ConversionRate;
                       }
                    }catch(Exception e){
                        return null;
                    }
                }
                if(CrAccountBalance>0 && TransactionBalance>0){
                    if(CrAccountBalance>TransactionBalance){
                        double newFromAccountBalance=0;
                        double newToAccountBalance=0;
                        if(FromAccount.getCurrency().equals(ToAccount.getCurrency())){
                            newFromAccountBalance=CrAccountBalance-TransactionBalance;
                            newToAccountBalance=ToAccount.getBalance()+TransactionBalance;
                        }else{
                            newFromAccountBalance=CrAccountBalance-transaction.getAmount();
                            newToAccountBalance=ToAccount.getBalance()+TransactionBalance;
                        }
                        DecimalFormat df =new DecimalFormat("#.##");
                        newFromAccountBalance=Double.valueOf(df.format(newFromAccountBalance));
                        newToAccountBalance=Double.valueOf(df.format(newToAccountBalance));
                        TransactionBalance=Double.valueOf(df.format(TransactionBalance));
                        FromAccount.setBalance(newFromAccountBalance);
                        ToAccount.setBalance(newToAccountBalance);
                        RPaccount.save(FromAccount);
                        RPaccount.save(ToAccount);
                        setAlternativeBalance(ToAccount.getUserEmail());
                        setAlternativeBalance(FromAccount.getUserEmail());
                        SaveTransaction newTransaction = new SaveTransaction();
                        newTransaction.setAmount(TransactionBalance);
                        newTransaction.setUserEmail(AccountOwner.getUserEmail());
                        LocalDateTime myDateObj = LocalDateTime.now();
                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        newTransaction.setDateandTime(myDateObj.format(myFormatObj));
                        newTransaction.setType("Transfer");
                        newTransaction.setCurrency(FromAccount.getCurrency());
                        RPtransaction.save(newTransaction);
                        return transaction;
                    }else{
                        return null;
                    }
                }else{
                     return null;
                }
            }
        }
        return null;
    }
   
}
