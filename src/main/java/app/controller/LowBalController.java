package app.controller;

import app.db.DBConnector;
import app.models.Products;
import app.services.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LowBalController {
    @FXML private TableView<Products> productsTable;
    @FXML private TableColumn<Products, Integer> colId;
    @FXML private TableColumn<Products, String> colName;
    @FXML private TableColumn<Products, Integer> colBalance;

    @FXML private Button btnCloseWindow;

    @FXML
    public void closeWindow(){
        parentController.closeLowBalList();
    }


    private ObservableList<Products> productsList = FXCollections.observableArrayList();
    private AdminViewController parentController = null;
    ProductService service = new ProductService();

    public void init(AdminViewController prntctrl) {
        this.parentController = prntctrl;

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colBalance.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colBalance.setOnEditCommit(event -> {
            Products item = event.getRowValue();
            if (event.getNewValue() == 0) {
                productsList.remove(item);
                service.deleteProduct(item.getId());
                File desc = new File("/mnt/data/Projects/Pixel Plus/src/main/resources/FileDescs/"
                        + item.getId() + ".txt");
                File image = new File("/mnt/data/Projects/Pixel Plus/src/main/resources/Images/Thumbnail/"
                        + item.getId() + ".jpg");
                desc.delete();
                image.delete();
            } else if (event.getNewValue() < 0) event.getOldValue();
            else {
                item.setBalance(event.getNewValue());
                service.updateBalance(event.getNewValue(), item.getId());
            }
        });

        productsList.addAll(service.fetchLowBal());
        productsTable.setItems(productsList);
    }


}
