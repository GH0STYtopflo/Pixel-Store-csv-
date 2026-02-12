package app.services;

import app.db.DBConnector;
import app.models.Transactions;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private String pathToUsersTable = "/mnt/data/Projects/Pixel-Store/tables/Users.csv";
    private String pathToTransactionsTable = "/mnt/data/Projects/Pixel-Store/tables/Transactions.csv";
    private String pathToProductsTable = "/mnt/data/Projects/Pixel-Store/tables/Products.csv";
    private String pathToCartsTable = "/mnt/data/Projects/Pixel-Store/tables/Carts.csv";

    public String totalRev(){
        List<String[]> allRows;
        String output = null;
        int total = 0;

        try(CSVReader reader = new CSVReader(new FileReader(pathToTransactionsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows){
                total += Integer.parseInt(row[1]);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        output = String.valueOf(total);
        return output;
    }

    public String totalOrder(){
        List<String[]> allRows;
        int total = 0;

        try (CSVReader reader = new CSVReader(new FileReader(pathToTransactionsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows) total++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(total);
    }

    public String totalPending(){
        List<String[]> allRows;
        ArrayList<String> uniqueRows = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(pathToCartsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows) {
                if (!uniqueRows.contains(row[0])) uniqueRows.add(row[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(uniqueRows.size());
    }

    public String totalUsers(){
        List<String[]> allRows;
        int total = 0;

        try (CSVReader reader = new CSVReader(new FileReader(pathToUsersTable))){
            allRows = reader.readAll();

            for (String[] row : allRows) total++;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(total);
    }

    public ObservableList<Transactions> transactions(){
        List<String[]> allRows;
        ObservableList<Transactions> transactions = FXCollections.observableArrayList();

        try (CSVReader reader = new CSVReader(new FileReader(pathToTransactionsTable))){
            allRows = reader.readAll();

            for (String[] row : allRows){
                transactions.add(new Transactions(Integer.parseInt(row[0]) , Integer.parseInt(row[1])
                        , Integer.parseInt(row[2]) , row[3]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return transactions;

    }
}
