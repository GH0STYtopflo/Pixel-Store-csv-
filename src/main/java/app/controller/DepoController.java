package app.controller;

import app.models.Users;
import app.services.Misc;
import app.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DepoController {


    @FXML private Button depositBtn;
    @FXML private Button cancelBtn;
    @FXML private TextField amountField;
    @FXML private Label msgText;

    @FXML public void onDepoClicked(){depoHandler();}
    @FXML public void handleCancel(){
        close();
    }

    private Users activeSession;
    private UserfrontController parentCtrlUserfront;
    private CheckoutController parentCtrlCheckout;
    UserService userService = new UserService();

    // This for when you open this pop up from the checkout page (for broke niggas)
    public void init(Users session , CheckoutController parentControllerCheckout, UserfrontController parentControllerUserFront){
        this.activeSession = session;
        this.parentCtrlUserfront = parentControllerUserFront;
        this.parentCtrlCheckout = parentControllerCheckout;
    }

    // This is for when you open depo pop up from the front page
    public void init(Users session , UserfrontController parentControllerUserFront){
        this.activeSession = session;
        this.parentCtrlUserfront = parentControllerUserFront;
    }



    public void depoHandler(){
        if (isValid(amountField.getText())) {
            userService.depo(activeSession.getId(), Integer.parseInt(amountField.getText()));
            activeSession.setBalance(activeSession.getBalance() + Integer.parseInt(amountField.getText()));

            msgText.setTextFill(Color.GREEN);
            msgText.setText("Deposited $" + Misc.separator(amountField.getText()));
            updateBalanceLabels();
            close();
        }
        else {
            msgText.setTextFill(Color.RED);
            msgText.setText("Please enter a valid amount");
        }
    }


    private void close(){
        if (parentCtrlCheckout == null) parentCtrlUserfront.closeDepoPopUp();
        else parentCtrlCheckout.closeDepoPopUp();
    }

    //Helper for checking if the entered value is valid
    private boolean isValid(String amountText){
        if (amountText.isBlank() || amountText.isEmpty()) return false;
        try {
            if (0 < Integer.parseInt(amountText)) return true;
            else return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //Helper for updating the balance labels across the app
    private void updateBalanceLabels(){
        if (parentCtrlCheckout == null){
            parentCtrlUserfront.updateBalance(Misc.separator(String.valueOf(activeSession.getBalance())));
        }
        else {
            parentCtrlCheckout.updateBalance(Misc.separator(String.valueOf(activeSession.getBalance())));
            parentCtrlUserfront.updateBalance(Misc.separator(String.valueOf(activeSession.getBalance())));
        }
    }

}
