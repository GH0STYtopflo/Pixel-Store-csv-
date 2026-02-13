package app.controller;

import app.models.Products;
import app.models.Users;
import app.services.CartService;
import app.services.Misc;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;

public class ProductDetailController {

    @FXML private Label brandLabel;
    @FXML private Label nameLabel;
    @FXML private Label ramLabel;
    @FXML private Label storageLabel;
    @FXML private Label stockLabel;
    @FXML private Label priceLabel;
    @FXML private Button addToCartBtn;
    @FXML private Button closeBtn;
    @FXML private Text descriptionText;
    @FXML private ImageView productImage;

    @FXML public void handleAddToCart(){addToCart();}
    @FXML public void handleClose(){close();}

    private Products product;
    private Users activeSession;
    private PhoneCardController parentController;

    private CartService cartService = new CartService();


    public void init (Products Currentproduct , PhoneCardController prntCtrl , Users session){
        this.product = Currentproduct;
        this.parentController = prntCtrl;
        this.activeSession = session;

        loadImg();
        loadDesc();
        priceLabel.setText("$" + Misc.separator(String.valueOf(product.getPrice())));
        brandLabel.setText(product.getBrand().toUpperCase().charAt(0) + product.getBrand().substring(1));
        ramLabel.setText(product.getRam() + "GB RAM");
        nameLabel.setText(product.getName());

        if (!inStockCheck()) {
            addToCartBtn.setDisable(true);
            stockLabel.setTextFill(Color.RED);
            stockLabel.setText("Out Of Stock");
        }

    }

    private void addToCart(){
        product.setBalance(product.getBalance() - 1);
        cartService.addToCart(activeSession.getId() , product);

        if (product.getBalance() != 0) changeButton();
        if (product.getBalance() == 0) {
            addToCartBtn.setDisable(true);
            addToCartBtn.setText("Out Of Stock");
            stockLabel.setText("Out Of Stock");
        }

    }

    private void close(){
        parentController.closeDetails();
        parentController.binder(product , activeSession);
    }

    // Check product balance in stock
    private boolean inStockCheck(){
        if (0 < product.getBalance()) return true;
        else return false;
    }

    // Image loader helper (I think I might be overdoin this clean code shi idk)
    private void loadImg(){
        String imgPath = "src/main/resources/Images/Thumbnail/" + product.getId() + ".jpg";
        File f = new File(imgPath);

        if (f.exists() && f.isFile()){
            Image image = new Image("file:" + imgPath);
            productImage.setImage(image);
            System.out.println("image exists");
        }
    }

    // Desc loader helper
    private void loadDesc(){
        String descPath = "src/main/resources/FileDescs/" + product.getId() + ".txt";
        StringBuilder desc = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(descPath))) {
            String line;
            line = reader.readLine();
            storageLabel.setText(line.substring(0 , line.indexOf("|")).trim());

            while ((line = reader.readLine()) != null){
                desc.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        descriptionText.setText(desc.toString().trim());
    }

    // Changes the "add to cart" button
    private void changeButton(){
        addToCartBtn.setStyle("-fx-background-color: #158B1B");
        addToCartBtn.setText("Added!");

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            addToCartBtn.setText("Add To Cart");
            addToCartBtn.setStyle("");
        });
        pause.play();
    }


}
