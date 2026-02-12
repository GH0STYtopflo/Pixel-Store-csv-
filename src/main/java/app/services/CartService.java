package app.services;

import app.db.DBConnector;
import app.models.Carts;
import app.models.Products;
import app.models.Users;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// header: cart_id , product_id, name, price , quantity
public class CartService {
    private String pathToCartsTable = "/mnt/data/Projects/Pixel-Store/tables/Carts.csv";
    private String pathToProductsTable = "/mnt/data/Projects/Pixel-Store/tables/Products.csv";
    private String pathToTransactionsTable = "/mnt/data/Projects/Pixel-Store/tables/Transactions.csv";

    // Cart insertion (cart id is user id)
    public void addToCart(int userID, Products product){
        List<String[]> allRows = List.of();

        try (CSVReader reader = new CSVReader(new FileReader(pathToCartsTable))){allRows = reader.readAll();}
        catch (Exception e){e.printStackTrace();}

        boolean exists = false;
        for (String[] row : allRows){
            if (row[1].equals(String.valueOf(product.getId())) &&
                    row[0].equals(String.valueOf(userID))){
                row[4] = String.valueOf((Integer.parseInt(row[4]) + 1));
                try (CSVWriter writer = new CSVWriter(new FileWriter(pathToCartsTable))){writer.writeAll(allRows);}
                catch (Exception e){e.printStackTrace();}

                exists = true;
                break;
            }
        }

        if (!exists) {
            String[] newItem = {String.valueOf(userID) , String.valueOf(product.getId())
                    , product.getName() , String.valueOf(product.getPrice()) , "1"};
            try (CSVWriter writer = new CSVWriter(new FileWriter(pathToCartsTable ,true))){
                writer.writeNext(newItem);
            }
            catch (Exception e){e.printStackTrace();}
        }

        updateProductBalance(product.getId());
    }

    public ObservableList getUserCart(int ID){
        ObservableList<Carts> cartItems = FXCollections.observableArrayList();
        List<String[]> allRows;

        try (CSVReader reader = new CSVReader(new FileReader(pathToCartsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows) {
                if (row[0].equals(String.valueOf(ID))) cartItems.add(new Carts(ID , Integer.parseInt(row[1]) ,
                        row[2] , Integer.parseInt(row[3]) , Integer.parseInt(row[4])));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return cartItems;
    }

    public void makePurchase(ObservableList<Carts> items){
        int userID = items.get(0).getCartID() , total = 1;
        List<String[]> allRows = List.of();
        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        for (Carts item : items){
            total *= item.getPrice() * item.getQuantity();
            for (String[] row : allRows) {
                if (item.getProductID() == Integer.parseInt(row[0])) row[5] =
                        String.valueOf(Integer.parseInt(row[5]) - 1);
                break;
            }
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToProductsTable))){
            writer.writeAll(allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearCart(userID);
        System.out.println("purchase made");

        addToTransactions(userID , total);
    }


    private void clearCart(int userID) {
        List<String[]> allRows = List.of();

        try (CSVReader reader = new CSVReader(new FileReader(pathToCartsTable))) {
            allRows = reader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String[] row : allRows) {
            if (Integer.parseInt(row[0]) == userID) allRows.remove(row);

            try (CSVWriter writer = new CSVWriter(new FileWriter(pathToCartsTable))) {
                writer.writeAll(allRows);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void clearCartAndReturn(int userID){
        ObservableList<Carts> items = getUserCart(userID);

        List<String[]> allRows = List.of();
        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){allRows = reader.readAll();}
        catch (Exception e){e.printStackTrace();}

        for (Carts item : items){
            for (String[] row : allRows) {
                if (item.getProductID() == Integer.parseInt(row[0])) {
                    row[5] = String.valueOf((Integer.parseInt(row[5]) + item.getQuantity()));
                    break;
                }
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToProductsTable))) {writer.writeAll(allRows);}
        catch (Exception e){e.printStackTrace();}

        clearCart(userID);

    }

    private void updateProductBalance(int productID){
        List<String[]> allRows = List.of();
        try (CSVReader reader = new CSVReader(new FileReader(pathToProductsTable))){
            allRows = reader.readAll();
            for (String[] row : allRows) {
                if (row[0].equals(String.valueOf(productID))) row[5] = String.valueOf((Integer.valueOf(row[5]) - 1));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToProductsTable))){
            writer.writeAll(allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToTransactions(int userID , int total){
        // header : num , total , customer id , date
        List<String[]> allRows;
        int num = 0;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        try (CSVReader reader = new CSVReader(new FileReader(pathToTransactionsTable))){
            allRows = reader.readAll();
            for (String[] row : allRows) num++;
            num++;
        }
        catch (IOException | CsvException e){
            e.printStackTrace();
        }

        String[] item = {String.valueOf(num) , String.valueOf(total) , String.valueOf(userID) , date};
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToTransactionsTable , true))){
            writer.writeNext(item);
            System.out.println("registered a new entry to the transactions table");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
