package app.models;

public class Products {
    private int id;
    private String name;
    private String brand;
    private int price;
    private int ram;
    private int balance;

    public Products(int id, String name, String brand, int price, int ram, int balance){
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.ram = ram;
        this.balance = balance;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getBrand() {return brand;}
    public void setBrand(String brand) {this.name = brand;}


    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}

    public int getRam() {return ram;}
    public void setRam(int ram) {this.balance = ram;}

    public int getBalance() {return balance;}
    public void setBalance(int balance) {this.balance = balance;}


}
