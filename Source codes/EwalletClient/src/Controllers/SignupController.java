/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import GUI.UserRegister;
import Models.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Syed Haseeb
 */
public class SignupController {
    private UserRegister RegisterUI;
    private User User;

    public SignupController() {
        this.RegisterUI=new UserRegister();
        this.User=new User();
        ShowUI();
        this.RegisterUI.addSubmitListener(new SubmitListener());
    }
    class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            Object arr[]=getUserInputs();
            User user=(User)arr[0];
            String currency=(String)arr[1];
            if(ValidateInputs(user,currency)){
                if(UserRegistration(user) && AccountRegistration(user,currency)){
                RegisterUI.DisplayInfo("Registration Successfully Completed");
                RegisterUI.setVisible(false);
                new LoginController();
                }
            }
        }
    }
    public void ShowUI(){
        this.RegisterUI.setVisible(true);
    }
    public boolean UserRegistration(User user){
        ObjectMapper mapper = new ObjectMapper();
        try{
            String JsonString = mapper.writeValueAsString(user);
            String API = "http://localhost:8000/api/v1/signup";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(API);
            StringEntity params =new StringEntity(JsonString);
            request.addHeader("content-type", "application/json");
            request.addHeader("Accept","application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            if(response.getStatusLine().getStatusCode()==200){
                return true;
            }else{
                RegisterUI.DisplayError("Email Address Already Exists in the Database");
            }
        }catch(Exception e){
            RegisterUI.DisplayError("Something went wrong connecting to the server");
        }
        return false;
    }
    
    public boolean AccountRegistration(User user,String currency){
        try{
            String JsonString ="{\"currency\": \""+ currency +"\",\"0\": 0,\"userEmail\": \""+ user.getUserEmail() +"\"}";
            String API = "http://localhost:8000/api/v1/createaccount";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(API);
            StringEntity params =new StringEntity(JsonString);
            request.addHeader("content-type", "application/json");
            request.addHeader("Accept","application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            if(response.getStatusLine().getStatusCode()==200){
                return true;
            }
        }catch(Exception e){}
        return false;
    }
    public boolean ValidateInputs(User user,String Currency){
        if(isValidEmail(user.getUserEmail())){
            if(!user.getUserPass().equals("") && !user.getUserFname().equals("") && !user.getUserLname().equals("") && user.getUserDob()!=null && !user.getUserGender().equals("") && !user.getUserNic().equals("") && !user.getUserMstatus().equals("") && !user.getUserAddress().equals("")&& !user.getUserCity().equals("")&& !Currency.equals("")){
               if(!user.getUserMstatus().equals("Select")){
                   if(Currency.equals("EUR") || Currency.equals("USD")){
                       return true;
                   }
               }else{
                   RegisterUI.DisplayError("Please Fill your Marital Status");
                   return false;
               } 
            }else{
                RegisterUI.DisplayError("Please Fill All Required Fields");
                return false;
            }
        }
        RegisterUI.DisplayError("Email Validation Failed");
        return false;
    }
    
    public Object[] getUserInputs(){
        User UserInouts=new User();
        UserInouts.setUserEmail(RegisterUI.getTxtEmail().getText());
        UserInouts.setUserPass(RegisterUI.getTxtPassword().getText());
        UserInouts.setUserFname(RegisterUI.getTxtFname().getText());
        UserInouts.setUserLname(RegisterUI.getTxtLname().getText());
        if(RegisterUI.getJdcBirthday().getDate()!=null){
            UserInouts.setUserDob(new SimpleDateFormat("yyyy-MM-dd").format(RegisterUI.getJdcBirthday().getDateEditor().getDate()));
        }
        boolean Male=RegisterUI.getChkMale().isSelected();
        boolean Female=RegisterUI.getChkFemale().isSelected();
        if(Male && !Female){
            UserInouts.setUserGender("Male");
        }
        if(!Male && Female){
            UserInouts.setUserGender("Female");
        }
        UserInouts.setUserNic(RegisterUI.getTxtNIC().getText());
        UserInouts.setUserMstatus(String.valueOf(RegisterUI.getCmbMarital().getSelectedItem()));
        UserInouts.setUserAddress(RegisterUI.getTxtAddress().getText());
        UserInouts.setUserCity(RegisterUI.getTxtCity().getText());
        String Currency=(String)RegisterUI.getCmbCurrency().getSelectedItem();
        Object arr[] = new Object[2];
        arr[0]=UserInouts; 
        arr[1]=Currency;
        return arr;
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
