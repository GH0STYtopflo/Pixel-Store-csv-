package app.controller;

import app.models.Users;
import app.services.IDGen;
import app.services.Misc;
import app.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AuthController {

    @FXML
    private TextField loginUser;
    @FXML
    private PasswordField loginPass;
    @FXML
    private Label subtext;
    @FXML
    private Label toggleHint;
    @FXML
    private Button toggleBtn;
    @FXML
    private Button signinup;
    @FXML
    private Label messagetext;

    UserService service = new UserService();

    Stage adminStage;


    @FXML public void ontoggleBtnClicked(){swichMode(signinup.getText());}
    @FXML public void onAuthClicked(){auth(signinup.getText());}


    public void swichMode(String mode){
        if (mode.equals("Login")) switchToSignup();
        else switchToLogin();
    }

    public void auth(String mode){
        if (mode.equals("Login")) login();
        else signup();
    }
    public void login(){
        Users user = service.findUserByUsername(loginUser.getText());

        if (loginPass.getText().isEmpty() || loginPass.getText().isBlank()) {
            messagetext.setTextFill(Color.RED);
            messagetext.setText("Password cannot be empty!");
        }
        else if (user == null) {
            messagetext.setTextFill(Color.RED);
            messagetext.setText("User not found!");
        }
        else if (!(Misc.encrypt(loginPass.getText().trim()).equals(user.getPassword()))) {
            messagetext.setTextFill(Color.RED);
            messagetext.setText("Invalid password");
        }
        else if (user != null) {
            messagetext.setTextFill(Color.GREEN);
            messagetext.setText("Login successful!");
            Users activeSession = service.findUserByUsername(loginUser.getText());
            if (activeSession.getRole().equals("customer")) {
                openFrontPage(activeSession);
            }
            else if (activeSession.getRole().equals("admin") || activeSession.getRole().equals("super")) {
                openAdminPanel(activeSession);
            }

        }
        else {
            messagetext.setTextFill(Color.RED);
            messagetext.setText("Something went wrong!");
        }

    }

    private void signup(){
        Users existingUser = service.findUserByUsername(loginUser.getText());
        if (loginPass.getText().isEmpty() || loginPass.getText().isBlank()) {
            messagetext.setTextFill(Color.RED);
            messagetext.setText("Password cannot be empty!");
        }
        else if (!passcheck(loginPass.getText())) {
            messagetext.setTextFill(Color.RED);
            messagetext.setText("Pick a stronger password.");
        }
        else if (existingUser != null) {
            messagetext.setTextFill(Color.RED);
            messagetext.setText("Username is not available.");
        }
        else if (existingUser == null) {
            register();
        }
        else {
            messagetext.setTextFill(Color.RED);
            messagetext.setText("Something went wrong!");
        }


    }

    public void closeAdminpanel(){
        adminStage.close();
    }

    // Helper to check password strength
    public boolean passcheck(String password){
        boolean status = false;
        if ((password.chars().anyMatch(Character::isDigit) ||
                !(password.toLowerCase().equals(password))) && !(password.length() < 6)) status = true;

        return status;
    }

    private void register(){
        service.registerUser(new Users(IDGen.gen() , loginUser.getText() , Misc.encrypt(loginPass.getText().trim()) , "customer" , 0));
        messagetext.setTextFill(Color.GREEN);
        messagetext.setText("Signed up successfully!");
        swichMode("Sign Up");
    }

    private void switchToSignup(){
        toggleBtn.setText("Login");
        toggleHint.setText("Already a member?");
        subtext.setText("Sign up. it's free!");
        signinup.setText("Sign Up");
    }

    private void switchToLogin(){
        toggleBtn.setText("New here?");
        toggleHint.setText("Create an account");
        subtext.setText("Login to your account");
        signinup.setText("Login");
    }

    private void openFrontPage(Users activeSession){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/usrfront.fxml"));
            Scene scene = new Scene(loader.load());
            UserfrontController uiController = loader.getController();
            uiController.init(activeSession);
            Stage stage = new Stage();
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.setTitle("Pixel Store");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAdminPanel(Users activeSession){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/admin_view.fxml"));
            Scene scene = new Scene(loader.load());
            AdminViewController uiController = loader.getController();
            uiController.init(activeSession , this);
            adminStage = new Stage();
            adminStage.setMaximized(true);
            adminStage.setScene(scene);
            adminStage.setTitle("Pixel Store");
            adminStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}