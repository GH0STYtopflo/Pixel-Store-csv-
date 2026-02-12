package app.controller;

import app.models.Products;
import app.models.Users;
import app.services.Misc;
import app.services.ProductService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

public class UserfrontController {
    @FXML private CheckBox googleCheck;
    @FXML private CheckBox samsungCheck;
    @FXML private CheckBox xiaomiCheck;
    @FXML private CheckBox oneplusCheck;
    @FXML private CheckBox appleCheck;
    @FXML private CheckBox othersCheck;
    @FXML private TextField lowbound;
    @FXML private TextField upbound;
    @FXML private CheckBox onetworam;
    @FXML private CheckBox twoeightram;
    @FXML private CheckBox eightplus;
    @FXML private Button applyfilter;
    @FXML private Button clearSearchBtn;
    @FXML private Button clearFilterBtn;
    @FXML private FlowPane phoneGallery;
    @FXML private TextField searchbox;
    @FXML private Button depoBtn;
    @FXML private Button checkoutBtn;



    private Users activeSession;



    ProductService service = new ProductService();

    @FXML public void onApplyFilterClicked() {filterResults();}

    @FXML public void onEnterPressed() {search();}

    @FXML public void onDepoClicked(){openDepoPopUp();}

    @FXML public void handleCheckout(){openCheckout();}

    @FXML public void handleClearFilters(){clearFilters();}

    @FXML public void handleClearSearch(){clearSearch();}

    @FXML public void handleOneTwo(){deselectRamBounds(onetworam);}
    @FXML public void handleTwoEight(){deselectRamBounds(twoeightram);}
    @FXML public void handleEightPlus(){deselectRamBounds(eightplus);}






    //init
    public void init(Users session){
        this.activeSession = session;
        depoBtn.setText("Balance: $" + Misc.separator(String.valueOf(activeSession.getBalance())));
        cardsLoader(service.fetchAll());
    }




    //Filters
    public void filterResults(){
        CheckBox[] ramBoundsCheckBoxes = {onetworam , twoeightram , eightplus};
        CheckBox[] brandCheckBoxes = {googleCheck , appleCheck , oneplusCheck ,
                othersCheck , samsungCheck , xiaomiCheck};

        int priceLow , priceHigh , ramLow = -1 , ramHigh = -1;


        ArrayList<String> checkedbrands = new ArrayList<>();
        for (CheckBox checkBox : brandCheckBoxes) if (checkBox.isSelected()) checkedbrands.add(checkBox.getText().toLowerCase());

        String priceText;
        if (!(lowbound.getText().isBlank() && upbound.getText().isBlank())){
            priceLow = Integer.parseInt(lowbound.getText());
            priceHigh = Integer.parseInt(upbound.getText());
        }
        else {
            priceLow = -1;
            priceHigh = -1;
        }

        if (onetworam.isSelected()){
            ramLow = 1;
            ramHigh = 2;
        } else if (twoeightram.isSelected()) {
            ramLow = 2;
            ramHigh = 8;
        }
        else if (eightplus.isSelected()) {
            ramLow = 8;
            ramHigh = 1000;
        }

        ArrayList<Products> results = service.filterResults(checkedbrands , priceLow , priceHigh , ramLow , ramHigh);
        cardsLoader(results);
    }

    //Clear filters
    private void clearFilters(){
        CheckBox[] ramBoundsCheckBoxes = {onetworam , twoeightram , eightplus};
        CheckBox[] brandCheckBoxes = {googleCheck , appleCheck , oneplusCheck ,
                othersCheck , samsungCheck , xiaomiCheck};

        for (CheckBox checkBox : ramBoundsCheckBoxes) checkBox.setSelected(false);
        for (CheckBox checkBox : brandCheckBoxes) checkBox.setSelected(false);
        upbound.clear();
        lowbound.clear();

        cardsLoader(service.fetchAll());
    }

    //Search box
    public void search(){
        ArrayList<Products> results;
        String searchText = searchbox.getText();
        if (isNumeric(searchText)) results = service.findByID(Integer.parseInt(searchText));
        else results = service.findByName(searchText.trim());

        if (!results.isEmpty()) {
            cardsLoader(results);
            clearSearchBtn.setVisible(true);
        }
        else {
            searchbox.setPromptText("Oops! no results found for " + searchbox.getText() + ".");
            searchbox.clear();
        }
    }

    //Clear Search
    private void clearSearch(){
        cardsLoader(service.fetchAll());
        clearSearchBtn.setVisible(false);
        searchbox.clear();
    }

    //GUI updater
    public void cardsLoader(ArrayList<Products> phones){
        // let's start clean young nih shall we?
        phoneGallery.getChildren().clear();

        if(phones != null) {
            for (Products phone : phones) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/card.fxml"));
                Node card = null;

                try {
                    card = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PhoneCardController controller = loader.getController();
                controller.binder(phone , activeSession);

                phoneGallery.getChildren().add(card);
            }
        }
    }

    Stage popUpstage;
    private void openDepoPopUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/deposit_view.fxml"));
            Parent root = loader.load();
            DepoController controller = loader.getController();
            controller.init(activeSession , this);
            popUpstage = new Stage();
            popUpstage.setTitle("Deposit");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popUpstage.setScene(scene);
            popUpstage.initStyle(StageStyle.TRANSPARENT);

            popUpstage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeDepoPopUp(){
        popUpstage.close();
    }

    public void updateBalance(String newBal){depoBtn.setText("Balance: $" + newBal);}


    Stage CheckoutStage;
    private void openCheckout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/cart_view.fxml"));
            Parent root = loader.load();
            CheckoutController controller = loader.getController();
            controller.init(activeSession , this);
            CheckoutStage = new Stage();
            CheckoutStage.setTitle("Checkout");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            CheckoutStage.setScene(scene);
            CheckoutStage.initStyle(StageStyle.TRANSPARENT);

            CheckoutStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeCheckout(){
        CheckoutStage.close();
    }





    //Helper for search
    private boolean isNumeric(String searchText){
        try {
            Integer.parseInt(searchText);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Helper for deselecting other ram bounds cause I'm evil
    private void deselectRamBounds(CheckBox theChosenOne){
        CheckBox[] ramBoundsCheckBoxes = {onetworam , twoeightram , eightplus};
        for (CheckBox checkBox : ramBoundsCheckBoxes) if (!checkBox.equals(theChosenOne)) checkBox.setSelected(false);
    }

}
