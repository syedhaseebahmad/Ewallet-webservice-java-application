/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import GUI.MainWindow;
import GUI.TransactionPanel;
import Models.Account;
import Models.Transaction;
import Models.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Sanjeewa Kulathunga
 */
public class MainWindowController {
    private MainWindow MainUI;
    private User User;
    private TransactionPanel TransactionUI;

    public MainWindowController(User User) {
        this.User = User;
        this.MainUI=new MainWindow();
        this.TransactionUI=new TransactionPanel();
        setDashboardValues();
        ShowUI();
        MainUI.addDepositListener(new DepositListener());
        MainUI.addWithdrawListener(new WithdrawListener());
        MainUI.addBillPaymentListener(new BillPaymentListener());
        MainUI.addLogoutListener(new LogoutListener());
        MainUI.addTranferListener(new TransferListener());
        MainUI.addDeleteAccListener(new DeleteAccountListener());
        TransactionUI.addTransactionListener(new TransactionListener());
        
    }
    
    class DeleteAccountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            int result =MainUI.DisplayConfirmDialog("Do you want to Delete your account");
            if(result==0){
                try {
                String API = "http://localhost:8000/api/v1/deleteaccount/"+User.getUserEmail();
                HttpClient httpClient = new DefaultHttpClient();
                HttpDelete request = new HttpDelete(API);
                request.addHeader("content-type", "application/json");
                request.addHeader("Accept","application/json");
                HttpResponse response = httpClient.execute(request);
                if (response.getStatusLine().getStatusCode() == 200){
                    MainUI.DisplayInfo("Account Delteted Sucessfully");
                    MainUI.dispose();
                    new LoginController();
                }else {
                    TransactionUI.DisplayError("Action Failed");
                    
                }
            } catch (Exception e) {}
            }
            else{
                
            }
        }
    }
    
    
    class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            MainUI.dispose();
            new LoginController();
        }
    }
    
    class DepositListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            TransactionUI.getLblTitle().setText("New Transaction: Deposit");
            TransactionUI.getLblType().setText("D");
            TransactionUI.getLblFrom().setText("To Account");
            TransactionUI.getLblTo().setVisible(false);
            TransactionUI.getCmbTo().setVisible(false);
            TransactionUI.getTxtTo().setVisible(false);
            if(TransactionUI.getCmbFrom().getItemCount()==0){
                TransactionUI.getCmbFrom().addItem(User.getUserEmail());
            }
            TransactionUI.setVisible(true);
        }
    }
    
    class WithdrawListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            TransactionUI.getLblTitle().setText("New Transaction: Withdraw");
            TransactionUI.getLblType().setText("W");
            TransactionUI.getLblFrom().setText("From Account");
            TransactionUI.getLblTo().setVisible(false);
            TransactionUI.getCmbTo().setVisible(false);
            TransactionUI.getTxtTo().setVisible(false);
            if(TransactionUI.getCmbFrom().getItemCount()==0){
                TransactionUI.getCmbFrom().addItem(User.getUserEmail());
            }
            TransactionUI.setVisible(true);
        }
    }
    
    class BillPaymentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            TransactionUI.getLblTitle().setText("New Transaction: Bill Payment");
            TransactionUI.getLblType().setText("B");
            TransactionUI.getLblFrom().setText("From Account");
            TransactionUI.getLblTo().setVisible(true);
            TransactionUI.getCmbTo().setVisible(true);
            TransactionUI.getTxtTo().setVisible(false);
            TransactionUI.getLblTo().setText("institute ");
            if(TransactionUI.getCmbFrom().getItemCount()==0){
                TransactionUI.getCmbFrom().addItem(User.getUserEmail());
            }
            TransactionUI.getCmbTo().removeAllItems();
            if(TransactionUI.getCmbTo().getItemCount()==0){
                TransactionUI.getCmbTo().addItem("Electricity");
                TransactionUI.getCmbTo().addItem("Broadband");
                TransactionUI.getCmbTo().addItem("Mobile");
                TransactionUI.getCmbTo().addItem("School");
            }
            TransactionUI.setVisible(true);
        }
    }
    
    class TransferListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            TransactionUI.getLblTitle().setText("New Transaction: Tranfer money");
            TransactionUI.getLblType().setText("T");
            TransactionUI.getLblFrom().setText("From Account");
            TransactionUI.getLblTo().setVisible(true);
            TransactionUI.getCmbTo().setVisible(false);
            TransactionUI.getTxtTo().setVisible(true);
            TransactionUI.getLblTo().setText("To Account");
            if(TransactionUI.getCmbFrom().getItemCount()==0){
                TransactionUI.getCmbFrom().addItem(User.getUserEmail());
            }
            TransactionUI.setVisible(true);
        }
    }
    
    class TransactionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            String TransType=TransactionUI.getLblType().getText();
            if(TransType.equals("D")){
                if(MakeDeposit()){
                    TransactionUI.DisplayInfo("Transaction Sucessfull");
                    MainUI.dispose();
                    TransactionUI.dispose();
                    new MainWindowController(User);
                }
            }
            if(TransType.equals("W")){
                if(MakeWithdraw()){
                    TransactionUI.DisplayInfo("Transaction Sucessfull");
                    MainUI.dispose();
                    TransactionUI.dispose();
                    new MainWindowController(User);
                }
            }
            if(TransType.equals("B")){
                if(PayBill()){
                    TransactionUI.DisplayInfo("Transaction Sucessfull");
                    MainUI.dispose();
                    TransactionUI.dispose();
                    new MainWindowController(User);
                }
            }
            if(TransType.equals("T")){
                if(TransferMoney()){
                    TransactionUI.DisplayInfo("Transaction Sucessfull");
                    MainUI.dispose();
                    TransactionUI.dispose();
                    new MainWindowController(User);
                }
            }
        }
    }
    
    public boolean TransferMoney(){
        Transaction UserTransaction=getTransactionInputs();
        if(ValidateTransactionInputs(UserTransaction)){
            ObjectMapper mapper = new ObjectMapper();
            try {
                String JsonString = mapper.writeValueAsString(UserTransaction);
                String API = "http://localhost:8000/api/v1/transfer";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(API);
                StringEntity params =new StringEntity(JsonString);
                request.addHeader("content-type", "application/json");
                request.addHeader("Accept","application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);
                if (response.getStatusLine().getStatusCode() == 200){
                    return true;
                }else {
                    TransactionUI.DisplayError("Transaction Failed");
                    return false;
                }
            } catch (Exception e) {}
        }
        return false;
    }
    
    public boolean PayBill(){
        Transaction UserTransaction=getTransactionInputs();
        if(ValidateTransactionInputs(UserTransaction)){
            ObjectMapper mapper = new ObjectMapper();
            try {
                String JsonString = mapper.writeValueAsString(UserTransaction);
                String API = "http://localhost:8000/api/v1/paybill";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(API);
                StringEntity params =new StringEntity(JsonString);
                request.addHeader("content-type", "application/json");
                request.addHeader("Accept","application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);
                if (response.getStatusLine().getStatusCode() == 200){
                    return true;
                }else {
                    TransactionUI.DisplayError("Transaction Failed");
                    return false;
                }
            } catch (Exception e) {}
        }
        return false;
    }
    
    public boolean MakeWithdraw(){
        Transaction UserTransaction=getTransactionInputs();
        if(ValidateTransactionInputs(UserTransaction)){
            ObjectMapper mapper = new ObjectMapper();
            try {
                String JsonString = mapper.writeValueAsString(UserTransaction);
                String API = "http://localhost:8000/api/v1/withdraw";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(API);
                StringEntity params =new StringEntity(JsonString);
                request.addHeader("content-type", "application/json");
                request.addHeader("Accept","application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);
                if (response.getStatusLine().getStatusCode() == 200){
                    return true;
                }else {
                    TransactionUI.DisplayError("Transaction Failed");
                    return false;
                }
            } catch (Exception e) {}
        }
        return false;
    }
    public boolean MakeDeposit(){
        Transaction UserTransaction=getTransactionInputs();
        if(ValidateTransactionInputs(UserTransaction)){
            ObjectMapper mapper = new ObjectMapper();
            try {
                String JsonString = mapper.writeValueAsString(UserTransaction);
                String API = "http://localhost:8000/api/v1/deposit";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(API);
                StringEntity params =new StringEntity(JsonString);
                request.addHeader("content-type", "application/json");
                request.addHeader("Accept","application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);
                if (response.getStatusLine().getStatusCode() == 200){
                    return true;
                }else {
                    TransactionUI.DisplayError("Transaction Failed");
                    return false;
                }
            } catch (Exception e) {}
        }
        return false;
    }
    
    public Transaction getTransactionInputs(){
        Transaction Userinputs=new Transaction();
        Userinputs.setFrom((String)TransactionUI.getCmbFrom().getSelectedItem());
        Userinputs.setTo((String)TransactionUI.getCmbTo().getSelectedItem());
        if(isNumeric(TransactionUI.getTxtAmount().getText())){
            Userinputs.setAmount(Double.parseDouble(TransactionUI.getTxtAmount().getText()));
        }else{
            Userinputs.setAmount(0);
        }
        if((String)TransactionUI.getCmbCurrency().getSelectedItem()!=""){
            Userinputs.setCurrency((String)TransactionUI.getCmbCurrency().getSelectedItem());
        }
        if(TransactionUI.getLblType().getText()=="D"){
            Userinputs.setType(1);
            Userinputs.setTo((String)TransactionUI.getCmbFrom().getSelectedItem());
        }
        if(TransactionUI.getLblType().getText()=="W"){
            Userinputs.setType(2);
        }
        if(TransactionUI.getLblType().getText()=="B"){
            Userinputs.setType(3);
        }
        if(TransactionUI.getLblType().getText()=="T"){
            Userinputs.setType(4);
            Userinputs.setTo(TransactionUI.getTxtTo().getText());
        }
        return Userinputs;
    }
    
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public boolean ValidateTransactionInputs(Transaction transaction){
        if(transaction.getFrom()!=""){
            if(!isValidEmail(transaction.getFrom())){
                TransactionUI.DisplayError("Please Select Your Ewallet Account");
                return false;
            }
        }
        if(transaction.getAmount()==0){
            TransactionUI.DisplayError("Amount validation failed");
            return false;
        }
        if(transaction.getCurrency()!="EUR" && transaction.getCurrency()!="USD"){
           TransactionUI.DisplayError("Select payment currency");
           return false;
        }
        return true;
    }
    
    public void ShowUI(){
        MainUI.setVisible(true);
    }
    
    public void setDashboardValues(){
        Account UserAccount=getUserBankAccount();
        if(UserAccount!=null){
            MainUI.getLblUserName().setText(User.getUserFname()+" "+User.getUserLname());
            MainUI.getLblUserEmail().setText(User.getUserEmail());
            MainUI.getLblAccBalance().setText(String.valueOf(UserAccount.getBalance()));
            MainUI.getLblAccCurrency().setText(UserAccount.getCurrency());
            MainUI.getLblAltBalance().setText(String.valueOf(UserAccount.getAltBalance()));
            if(UserAccount.getCurrency().equals("EUR")){
                MainUI.getLblAltCurrency().setText("USD");
            }
            if(UserAccount.getCurrency().equals("USD")){
                MainUI.getLblAltCurrency().setText("EUR");
            }
        }else{
            MainUI.dispose();
            System.exit(0);
        }
    }
    public Account getUserBankAccount(){
        try{
            String API = "http://localhost:8000/api/v1/accountbyid/"+User.getUserEmail();
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(API);
            HttpResponse response = httpClient.execute(request);
            if(response.getStatusLine().getStatusCode()==200){
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                ObjectMapper objectMapper = new ObjectMapper();
                Account CurrentUserAccount = objectMapper.readValue(responseString, Account.class);
                return CurrentUserAccount;
            }else{
                return null;
            }
        }catch(Exception e){}
        return null;
    }
    
    public boolean isValidEmail(String Email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(Email).matches();
    }
    
}
