package app.controller;

import app.models.Transactions;
import app.models.Users;
import app.services.AdminService;
import app.services.Misc;
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
import javafx.stage.StageStyle;

import java.sql.SQLException;
import java.time.LocalDate;


public class AdminViewController {

    @FXML private Label lblTotalRevenue;
    @FXML private Label lblTotalOrders;
    @FXML private Label lblTotalPending;
    @FXML private Label lblActiveUsers;
    @FXML private Label wlcmMessage;
    @FXML private Label lblCurrentDate;
    @FXML private Button btnDashboard;
    @FXML private Button btnLowBalance;
    @FXML private Button btnUsers;
    @FXML private Button btnProducts;
    @FXML private Button btnAddProduct;
    @FXML private Button btnLogOut;

    @FXML private TableView<Transactions> salesTable;
    @FXML private TableColumn<Transactions, Integer> colId;
    @FXML private TableColumn<Transactions, Integer> colTotal;
    @FXML private TableColumn<Transactions, Integer> colCustomer;
    @FXML private TableColumn<Transactions, String> colDate;

    @FXML public void showUsers() {
        openUsersList();
    }
    @FXML public void showProducts() {
        openProductsList();
    }
    @FXML public void showAddProduct() {
        addProducts();
    }
    @FXML public void handleLogout() {
        signOut();
    }
    @FXML public void showLowBalance() {openLowBalList();}



    private Users activeSession;
    private AuthController parentcontroller;
    AdminService adminService = new AdminService();
    private ObservableList<Transactions> transactionslist = FXCollections.observableArrayList();


    public void init(Users session, AuthController prntCtrl) throws SQLException {
        this.activeSession = session;
        this.parentcontroller = prntCtrl;

        loadStats();

        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colId.setCellValueFactory(new PropertyValueFactory<>("number"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        loadTrnscTable();
        salesTable.setItems(transactionslist);
    }


    Stage usersList;
    private void openUsersList() {
        try {
            btnUsers.setStyle("-fx-background-color:  #EFEBE9");
            FXMLLoader usersListLoader = new FXMLLoader(getClass().getResource("/app/view/users_list_view.fxml"));
            Parent root = usersListLoader.load();
            UsersListController usersListController = usersListLoader.getController();
            usersListController.init(this , activeSession);
            usersList = new Stage();
            usersList.setTitle("Users List");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            usersList.setScene(scene);
            usersList.initStyle(StageStyle.TRANSPARENT);

            usersList.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeUsersList() {
        usersList.close();
        btnUsers.setStyle("-fx-background-color:  transparent");
    }


    Stage productsList;
    private void openProductsList() {
        try {
            btnProducts.setStyle("-fx-background-color:  #EFEBE9");
            FXMLLoader productsListloader = new FXMLLoader(getClass().getResource("/app/view/products_list_view.fxml"));
            Parent root = productsListloader.load();
            ProductsListController productsListController = productsListloader.getController();
            productsListController.init(this);
            productsList = new Stage();
            productsList.setTitle("Products List");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            productsList.setScene(scene);
            productsList.initStyle(StageStyle.TRANSPARENT);

            productsList.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeProductsList() {
        productsList.close();
        btnProducts.setStyle("-fx-background-color:  transparent");
    }

    Stage lowBalList;
    private void openLowBalList() {
        try {
            btnLowBalance.setStyle("-fx-background-color:  #EFEBE9");
            FXMLLoader lowBalListLoader = new FXMLLoader(getClass().getResource("/app/view/lowbalance_view.fxml"));
            Parent root = lowBalListLoader.load();
            LowBalController lowBalController = lowBalListLoader.getController();
            lowBalController.init(this);
            lowBalList = new Stage();
            lowBalList.setTitle("Products List");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            lowBalList.setScene(scene);
            lowBalList.initStyle(StageStyle.TRANSPARENT);

            lowBalList.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeLowBalList() {
        lowBalList.close();
        btnLowBalance.setStyle("-fx-background-color:  transparent");
    }

    Stage addProduct;
    private void addProducts() {
        try {
            btnAddProduct.setStyle("-fx-background-color:  #EFEBE9");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/add_product_view.fxml"));
            Parent root = loader.load();
            AddProductViewController addProductViewController = loader.getController();
            addProductViewController.init(this);
            addProduct = new Stage();
            addProduct.setTitle("Add Product");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            addProduct.setScene(scene);
            addProduct.initStyle(StageStyle.TRANSPARENT);

            addProduct.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeAddProduct() {
        addProduct.close();
        btnAddProduct.setStyle("-fx-background-color:  transparent");
    }

    public void loadTrnscTable() {transactionslist = adminService.transactions();}


    public void signOut() {parentcontroller.closeAdminpanel();}

    // Loads stats and stuff
    private void loadStats(){
        wlcmMessage.setText("Welcome back, " + activeSession.getUsername());
        String totalSales = Misc.separator(adminService.totalRev());
        if (totalSales == null) totalSales = "0";
        lblTotalRevenue.setText("$" + totalSales);
        lblTotalOrders.setText(Misc.separator(adminService.totalOrder()));
        lblTotalPending.setText(Misc.separator(adminService.totalPending()) + " Pending");
        lblActiveUsers.setText(Misc.separator(adminService.totalUsers()));
        LocalDate date = LocalDate.now();
        lblCurrentDate.setText(date.toString());
    }





}
