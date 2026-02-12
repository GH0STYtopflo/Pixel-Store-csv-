package app.controller;

import app.models.Carts;
import app.models.Users;
import app.services.CartService;
import app.services.Misc;
import app.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckoutController {
    @FXML private TableView<Carts> cartTable;
    @FXML private TableColumn<Carts , String> colName;
    @FXML private TableColumn<Carts , Integer> colPrice;
    @FXML private TableColumn<Carts , Integer> colQty;
    @FXML private Button balanceBtn;
    @FXML private Button checkoutBtn;
    @FXML private Label totalLabel;
    @FXML private Label msgLabel;
    @FXML private Label userBalanceLabel;
    @FXML private Button closeBtn;
    @FXML private Button clearCartBtn;


    @FXML public void handleCheckout(){checkout();}
    @FXML public void handleDeposit(){openDepoPopUp();}
    @FXML public void handleClose(){closeCheckout();}
    @FXML public void handleClearCart(){clearCart();}

    private CartService cartService = new CartService();
    private UserService userService = new UserService();
    private Users activeSession;
    private UserfrontController parentController;
    private int total = 0;

    private ObservableList<Carts> cart = FXCollections.observableArrayList();


    public void init(Users session , UserfrontController prntCtrl){
        activeSession = session;
        this.parentController = prntCtrl;
        tableviewSetup();
        loadCartData();
        if (cart.isEmpty()) total = 0;

        cartTable.setItems(cart);
        userBalanceLabel.setText("$" + Misc.separator(String.valueOf(activeSession.getBalance())));
        totalLabel.setText("$" + Misc.separator(String.valueOf(total)));

    }

    private void loadCartData(){
        cart = cartService.getUserCart(activeSession.getId());
        for (Carts item : cart) total += item.getPrice() * item.getQuantity();
    }

    // This where broke niggs deposit money
    Stage popUpstage;
    private void openDepoPopUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/deposit_view.fxml"));
            Parent root = loader.load();
            DepoController controller = loader.getController();
            controller.init(activeSession , this , parentController);
            popUpstage = new Stage();
            popUpstage.setTitle("Deposit");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popUpstage.setScene(scene);
            popUpstage.initStyle(javafx.stage.StageStyle.TRANSPARENT);

            popUpstage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeDepoPopUp(){
        popUpstage.close();
    }

    private void closeCheckout(){parentController.closeCheckout();}

    private void clearCart(){
        cartService.clearCartAndReturn(activeSession.getId());
        cart.clear();
        parentController.init(activeSession);
        totalLabel.setText("$0");
    }

    private void checkout(){
        if (total <= activeSession.getBalance()) {
            cartService.makePurchase(cart);
            msgLabel.setTextFill(Color.GREEN);
            msgLabel.setText("Purchase made. Thanks for choosing us!");
            parentController.updateBalance(String.valueOf(activeSession.getBalance() - total));
            userService.withdraw(activeSession.getId() , total);
            activeSession.setBalance(activeSession.getBalance() - total);
            parentController.init(activeSession);
            updateBalance(String.valueOf(activeSession.getBalance()));
            cart.clear();
            totalLabel.setText("$0");
        }
        else {
            msgLabel.setTextFill(Color.RED);
            msgLabel.setText("Insufficient balance.");
        }
    }

    public void updateBalance(String newBal){userBalanceLabel.setText("$" + newBal);}

    // Helper for setting the table view up
    private void tableviewSetup(){
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }


}
