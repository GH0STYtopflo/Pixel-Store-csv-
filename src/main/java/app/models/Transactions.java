package app.models;

public class Transactions {
    private int number;
    private int total;
    private int customer;
    private String date;

    public Transactions(int num , int total, int customerID , String date){
        this.customer = customerID;
        this.date = date;
        this.number = num;
        this.total = total;
    }

    public int getNumber() {
        return number;
    }

    public int getTotal() {
        return total;
    }

    public int getCustomer() {
        return customer;
    }

    public String getDate() {
        return date;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
