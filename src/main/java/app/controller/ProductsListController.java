package app.controller;

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

public class ProductsListController {


    @FXML private TableView<Products> productsTable;
    @FXML private TableColumn<Products, Integer> colId;
    @FXML private TableColumn<Products, String> colName;
    @FXML private TableColumn<Products, String> colBrand;
    @FXML private TableColumn<Products, Integer> colBalance;
    @FXML private TableColumn<Products, Integer> colRam;
    @FXML private TableColumn<Products, Integer> colPrice;
    @FXML private Button btnCloseWindow;

    @FXML public void closeWindow(){parentController.closeProductsList();}

    private ObservableList<Products> productsList = FXCollections.observableArrayList();
    private AdminViewController parentController = null;
    ProductService service = new ProductService();

    public void init(AdminViewController prntctrl){
        this.parentController = prntctrl;

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnSetup();
        balanceColumnSetup();
        priceColSetup();
        colRam.setCellValueFactory(new PropertyValueFactory<>("ram"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));

        productsList.addAll(service.fetchAll());
        productsTable.setItems(productsList);
    }

    private void nameColumnSetup(){
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            Products item = event.getRowValue();

            item.setName(event.getNewValue());
            service.updateName(event.getNewValue() , item.getId());
        });
    }

    private void balanceColumnSetup(){
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colBalance.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colBalance.setOnEditCommit(event -> {
            Products item = event.getRowValue();
            if (event.getNewValue() == 0) {
                productsList.remove(item);
                service.deleteProduct(item.getId());
                File desc = new File("src/main/resources/FileDescs/"
                        + item.getId() + ".txt");
                File image = new File("src/main/resources/Images/Thumbnail/"
                        + item.getId() + ".jpg");
                desc.delete();
                image.delete();
            }
            else if (event.getNewValue() < 0) item.setBalance(event.getOldValue());
            else {
                item.setBalance(event.getNewValue());
                service.updateBalance(event.getNewValue(), item.getId());
            }
        });
    }

    private void priceColSetup(){
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colPrice.setOnEditCommit(event -> {
            Products item = event.getRowValue();
            if (0 < event.getNewValue()) item.setPrice(event.getNewValue());
            service.updatePrice(event.getNewValue() , item.getId());
        });
    }
}
