/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import GUI.Login;
import Models.User;
import Models.UserLogin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Syed Haseeb
 */
public class LoginController {
    private UserLogin LoginModel;
    private Login LoginUI;

    public LoginController() {
        this.LoginModel=new UserLogin();
        this.LoginUI=new Login();
        this.LoginUI.addCreateAccountListener(new CreateAccountListener());
        this.LoginUI.addLoginListener(new CreateLoginListener());
        ShowUI();
    }
    class CreateLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            User CurrentUser=null;
            CurrentUser=UserLogin();
            if(CurrentUser != null){
                LoginUI.setVisible(false);
                new MainWindowController(CurrentUser);
            }
        }
    }
    class CreateAccountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            LoginUI.setVisible(false);
            new SignupController();
        }   
    }
    public void ShowUI(){
        LoginUI.setVisible(true);
    }
    public User UserLogin(){
        if(getUserInput()){
            ObjectMapper mapper = new ObjectMapper();
            try{  
                String JsonString = mapper.writeValueAsString(LoginModel);
                String API = "http://localhost:8000/api/v1/login";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(API);
                StringEntity params =new StringEntity(JsonString);
                request.addHeader("content-type", "application/json");
                request.addHeader("Accept","application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);
                if(response.getStatusLine().getStatusCode()==200){
                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    ObjectMapper objectMapper = new ObjectMapper();
                    User CurrentUser = objectMapper.readValue(responseString, User.class);
                    LoginUI.setVisible(false);
                    new MainWindowController(CurrentUser);
                }else{
                    LoginUI.DisplayError("Incorrect email or password");
                }
            }catch(Exception e){
                LoginUI.DisplayError("Something went wrong connecting to the server");
            }
        }
        return null;
    }
    
    public boolean getUserInput(){
        String UserEmail="";
        String UserPassword="";
        try{
            UserEmail=LoginUI.getTxtEmail().getText();
            UserPassword=LoginUI.getTxtPassword().getText();
        }catch(Exception e){}
        if(!UserEmail.equals("") && !UserPassword.equals("")){
            if(isValidEmail(UserEmail)){
                LoginModel.setUserEmail(UserEmail);
                LoginModel.setUserPassword(UserPassword);
                return true;
            }else{
                LoginUI.DisplayError("Email Validation Failed");
            }
        }else{
            LoginUI.DisplayError("Please Fill All Required Fields");
        }
        return false;
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
