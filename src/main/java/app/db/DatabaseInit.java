package app.db;

import app.services.Misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DatabaseInit {

    public static void init() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role TEXT NOT NULL,
                balance INTEGER DEFAULT 0                            
            );
        """;

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Users table created or already exists");

        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = "INSERT INTO users (id , username, password, role, balance) VALUES (0, 'GH0STYtopflo', ? , 'super', 1000000)";
        try (Connection conn = DBConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1 , Misc.encrypt("nahfr"));

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = """
            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                brand TEXT NOT NULL,
                price INTEGER NOT NULL,
                ram INTEGER NOT NULL,
                balance INTEGER DEFAULT 0                            
            );
        """;

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Products table created or already exists");

        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = """
            CREATE TABLE IF NOT EXISTS transactions (
                number INTEGER PRIMARY KEY,
                total INTEGER NOT NULL,
                customer INTEGER NOT NULL,
                date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP                            
            );
        """;

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("transactions table created or already exists");

        } catch (Exception e) {
            e.printStackTrace();
        }


        //important! cart id is user id
        sql = """
            CREATE TABLE IF NOT EXISTS carts (
                cart_id INTEGER PRIMARY_KEY,
                product_id INTEGER NOT NULL,
                name TEXT NOT NULL,
                price INTEGER NOT NULL,
                quantity INTEGER NOT NULL CHECK (quantity > 0),
                
                UNIQUE (product_id)     
            );
        """;

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("carts table created or already exists");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}