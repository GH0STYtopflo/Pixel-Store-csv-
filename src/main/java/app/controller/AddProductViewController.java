package app.controller;

import app.models.Products;
import app.services.IDGen;
import app.services.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AddProductViewController {
    @FXML private TextField nameField;
    @FXML private TextField ramField;
    @FXML private TextField priceField;
    @FXML private TextField qtyField;
    @FXML private TextArea descArea;
    @FXML private TextField brandAcc;
    @FXML private Label bigAssPlusSign;
    @FXML private Label imgAreaText;
    @FXML private Label msgLabel;
    @FXML private ImageView previewImage;
    @FXML private Button addBtn;
    @FXML private Button closeBtn;
    @FXML private StackPane dropZone;
    @FXML private VBox uploadPlaceholder;


    private AdminViewController parentController;
    private File productImage;
    private Products product;
    private ProductService productService = new ProductService();


    @FXML public void onAddClicked(){
        insertIntoDB();
        close();
    }
    @FXML public void onCloseClicked(){close();}

    @FXML public void handleDragOver(DragEvent event) {
        changeStyle(event);
        event.consume();
    }


    @FXML public void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasFiles()) {
            File file = db.getFiles().get(0);
            productImage = file;

            String fileName = file.getName().toLowerCase();
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){

                Image image = new Image(file.toURI().toString());
                previewImage.setImage(image);

                previewImage.setVisible(true);
                uploadPlaceholder.setVisible(false);
                success = true;
            }
        }
        event.setDropCompleted(success);
        event.consume();
        dropZone.setStyle("");
    }


    public void init(AdminViewController prntCtrl) {
        parentController = prntCtrl;
    }




    public void saveImg(){
        String name = product.getId() + getFileExtention(productImage) , dir = "src/main/resources/Images/Thumbnail/";

        File directory = new File(dir);
        if (!directory.exists()) directory.mkdirs();

        Path source = productImage.toPath();
        Path destination = Paths.get(dir + name);

        try {
            Files.copy(source , destination , StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void insertIntoDB(){
        int ID = IDGen.gen();

        while (!productService.idIsUnique(ID)) {
            ID = IDGen.gen();
        }
        product = new Products(ID, nameField.getText(), brandAcc.getText().toLowerCase(),
                Integer.parseInt(priceField.getText()), Integer.parseInt(ramField.getText()),
                Integer.parseInt(qtyField.getText()));

        productService.registerProduct(product, descArea.getText());
        saveImg();
        close();
    }

    private void close(){
        parentController.closeAddProduct();
    }

    private void changeStyle(DragEvent event){
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);

            dropZone.setStyle("-fx-background-color: #EFEBE9; " +
                    "-fx-border-color: #3E2723; " +
                    "-fx-border-style: dashed; " +
                    "-fx-border-width: 3; " +
                    "-fx-background-radius: 24; " +
                    "-fx-border-radius: 24;");
        }
        else {
            dropZone.setStyle("-fx-background-color: #EFEBE9; " +
                    "-fx-border-color: #FF0000; " +
                    "-fx-border-style: dashed; " +
                    "-fx-border-width: 3; " +
                    "-fx-background-radius: 24; " +
                    "-fx-border-radius: 24;");
        }
    }

    //Helper
    private String getFileExtention(File file){
        String name = file.getName();
        name = name.substring(name.indexOf("."));


        return name;
    }

}
