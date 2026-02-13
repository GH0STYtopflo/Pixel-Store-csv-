package app.db;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DatabaseInit {
    private static String pathToUsersTable = "tables/Carts.csv";
    private static String pathToProductsTable = "tables/Products.csv";
    private static String pathToCartsTable = "tables/Carts.csv";
    private static String pathToTransactionsTable = "tables/Transactions.csv";




    public static void init(){
        usersTableSetup();
        productsTableSetup();
        cartsTableSetup();
        transactionsTableSetup();
    }

    private static void usersTableSetup(){
        File users = new File(pathToUsersTable);

        if(!users.exists()){
            try {
                if (users.createNewFile()) System.out.println("Table created successfully");
            } catch (IOException e) {
                System.out.println("Something went wrong");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToUsersTable))) {
                writer.write("id,username,password,balance,role");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else System.out.println("Users table already exists");
    }

    private static void productsTableSetup(){
        File users = new File(pathToProductsTable);

        if(!users.exists()){
            try {
                if (users.createNewFile()) System.out.println("Table created successfully");
            } catch (IOException e) {
                System.out.println("Something went wrong");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToProductsTable))) {
                writer.write("id,name,brand,price,ram,balance");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else System.out.println("Products table already exists");
    }

    private static void cartsTableSetup(){
        File users = new File(pathToCartsTable);

        if(!users.exists()){
            try {
                if (users.createNewFile()) System.out.println("Carts created successfully");
            } catch (IOException e) {
                System.out.println("Something went wrong");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToCartsTable))) {
                writer.write("cart_id,product_id,name,price,quantity");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else System.out.println("Carts table already exists");
    }

    private static void transactionsTableSetup(){
        File users = new File(pathToTransactionsTable);

        if(!users.exists()){
            try {
                if (users.createNewFile()) System.out.println("Table created successfully");
            } catch (IOException e) {
                System.out.println("Something went wrong");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToTransactionsTable))) {
                writer.write("num,total,customer_id,date");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else System.out.println("Transactions table already exists");
    }
}