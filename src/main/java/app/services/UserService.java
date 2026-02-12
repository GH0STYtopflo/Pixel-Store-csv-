package app.services;

import app.db.DBConnector;
import app.models.Users;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private String pathToUsersTable = "/mnt/data/Projects/Pixel-Store/tables/Users.csv";

    //User Insertion
    public void registerUser(Users user) {
        String[] userdata = {String.valueOf(user.getId()) , user.getUsername() , user.getPassword()
                , "0" , user.getRole()};
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToUsersTable , true))){
            writer.writeNext(userdata);
            System.out.println("Registered a new user");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //User Finder
    public Users findUserByUsername(String username) {
        Users user = null;

        try (CSVReader reader = new CSVReader(new FileReader(pathToUsersTable))){
            List<String[]> allRows = reader.readAll();

            for (String[] row : allRows){
                if (row[1].equals(username)) {
                    user = new Users(Integer.parseInt(row[0]) , row[1] , row[2] , row[4], Integer.parseInt(row[3]));
                    System.out.println("Found user");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    //User Deletion
    public void deleteUser(int userId) {
        List<String[]> allRows = null;
        try (CSVReader reader = new CSVReader(new FileReader(pathToUsersTable))){
            allRows = reader.readAll();

            for (String[] row : allRows){
                if (row[0].equals(String.valueOf(userId))) {
                    allRows.remove(row);
                    System.out.println("Deleted user with id: " + userId);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToUsersTable))){
            writer.writeAll(allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Deposit
    public void depo(int userId, int amount) {
        List<String[]> allRows = null;

        try(CSVReader reader = new CSVReader(new FileReader(pathToUsersTable))){
            allRows = reader.readAll();
            int newBalance;

            for (String[] row : allRows){
                if (row[0].equals(String.valueOf(userId))) {
                    newBalance = Integer.parseInt(row[3]) + amount;
                    row[3] = String.valueOf(newBalance);
                    System.out.println("deposited $" + amount);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToUsersTable))) {
            writer.writeAll(allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Withdraw
    public void withdraw(int userId, int amount) {
        List<String[]> allRows = null;

        try(CSVReader reader = new CSVReader(new FileReader(pathToUsersTable))){
            allRows = reader.readAll();

            for (String[] row : allRows){
                if (row[0].equals(String.valueOf(userId))) row[3] = String.valueOf(amount - Integer.parseInt(row[3]));
                System.out.println("withdrew $" + amount);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToUsersTable))) {
            writer.writeAll(allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update role
    public void updateRole(String role, int userID){
        List<String[]> allRows = null;

        try(CSVReader reader = new CSVReader(new FileReader(pathToUsersTable))){
            allRows = reader.readAll();

            for (String[] row : allRows){
                if (row[0].equals(String.valueOf(userID))) row[4] = role;
                System.out.println("updated user role");
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToUsersTable))) {
            writer.writeAll(allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // fetch all users
    public ArrayList<Users> fetchAll(){
        List<String[]> allRows;
        ArrayList<Users> results = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(pathToUsersTable))){
            allRows = reader.readAll();

            for (String[] row : allRows) results.add(new Users(Integer.parseInt(row[0]) ,row[1] ,
                    row[2], row[4], Integer.parseInt(row[3])));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}