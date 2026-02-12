package app.controller;

import app.models.Products;
import app.models.Users;
import app.services.CartService;
import app.services.Misc;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;

public class PhoneCardController {
    @FXML public Label descText;
    @FXML public Label nameLabel;
    @FXML public Label priceLabel;
    @FXML public Label msg;
    @FXML public ImageView phoneImage;
    @FXML public Button buyButton;
    @FXML public Button detailsBtn;

    private Users activeSession;
    private Products product;

    @FXML public void handleAddToCart(){addToCart();}

    @FXML public void handleDetails(){openDetails();}


    // Loads the product info. I mean, isn't it obvious?
    public void binder(Products product , Users session){
        this.activeSession = session;
        this.product = product;

        nameLabel.setText(product.getName());
        priceLabel.setText("$" + Misc.separator(String.valueOf(product.getPrice())));
        loadDescSnippet();
        loadImg();
        checkInstock();
    }

    // For opening the product's introduction page
    Stage detail;
    private void openDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/detail.fxml"));
            Parent root = loader.load();
            ProductDetailController controller = loader.getController();
            controller.init(product , this , activeSession);
            detail = new Stage();
            detail.setTitle("Product Info");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            detail.setScene(scene);
            detail.initStyle(javafx.stage.StageStyle.TRANSPARENT);

            detail.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeDetails(){detail.close();}

    // The next 2 methods are for loading product's data.
    private void loadDescSnippet(){
        try (BufferedReader reader = new BufferedReader(new FileReader("/mnt/data/Projects/Pixel Plus/src/main/resources/FileDescs/"
                + product.getId() + ".txt"));) {
            descText.setText(reader.readLine().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImg(){
        String path = "src/main/resources/Images/Thumbnail/" + String.valueOf(product.getId()) + ".jpg";
        File imgFile = new File(path);

        if (imgFile.isFile() && imgFile.exists()){
            Image image = new Image("file:" + path);

            phoneImage.setImage(image);
        }
    }


    // Handles the process of adding the product to users cart.
    private void addToCart(){
        product.setBalance(product.getBalance() - 1);
        CartService service = new CartService();
        service.addToCart(activeSession.getId() , product);
        changeButton();
        checkInstock();
    }

    // Checks the balance of product and disables buttons if it's necessary.
    private void checkInstock(){
        if (product.getBalance() == 0) {
            buyButton.setText("Out of stock");
            buyButton.setStyle("-fx-font-size: 11px");
            buyButton.setDisable(true);
            msg.setVisible(false);
        }
        else if (product.getBalance() <= 5) {
            msg.setTextFill(Color.RED);
            msg.setText("Only " + product.getBalance() + " units left!");
            msg.setVisible(true);
        }
    }

    // The magic behind the add button.
    private void changeButton(){
        buyButton.setStyle("-fx-background-color: #158B1B; -fx-font-size: 12px");
        buyButton.setText("Added!");
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            buyButton.setStyle("");
            if (product.getBalance() != 0) buyButton.setText("Add");
            else buyButton.setStyle("-fx-font-size: 11px");
        });
        pause.play();
    }



}

