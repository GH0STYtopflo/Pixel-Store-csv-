package app.models;

public class Users {
    private int id;
    private String username;
    private String password;
    private String role;
    private int balance;

    public Users(int id, String username, String password, String role , int balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.balance = balance;
    }
    public Users(int id, String username, String role , int balance) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.balance = balance;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getBalance(){return balance;}

    public void setBalance(int balance){this.balance = balance;}
}