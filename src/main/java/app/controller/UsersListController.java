package app.controller;

import app.db.DBConnector;
import app.models.Users;
import app.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UsersListController {


    @FXML private TableView<Users> usersTable;
    @FXML private TableColumn<Users, Integer> colId;
    @FXML private TableColumn<Users, String> colName;
    @FXML private TableColumn<Users, Integer> colBalance;
    @FXML private TableColumn<Users, String> colRole;

    @FXML private Button btnCloseWindow;

    private ObservableList<Users> userList = FXCollections.observableArrayList();

    private AdminViewController parentController;

    private Users activeSession;

    private UserService service = new UserService();


    public void init(AdminViewController prntctrl , Users session) {
        parentController = prntctrl;
        userList.addAll(service.fetchAll());
        activeSession = session;

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colRole.setCellFactory(TextFieldTableCell.forTableColumn());
        colRole.setOnEditCommit(event -> {
            Users user = event.getRowValue();
            String newRole = event.getNewValue().toLowerCase();
            if (activeSession.getRole().equals("super")){
                if (newRole.equals("admin") || newRole.equals("super") || newRole.equals("customer")) {
                    user.setRole(newRole);
                    service.updateRole(newRole , user.getId());
                }
            }

        });

        usersTable.setItems(userList);
    }


    @FXML
    public void closeWindow(){
        parentController.closeUsersList();
    }
}