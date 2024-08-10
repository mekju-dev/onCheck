package com.wgu.testing123;

public class Loan {
    int id;
    String item;
    int itemID;
    int quantity;
    String driver;
    int driverID;
    String date;

    public Loan(int id, String item, int itemID, int quantity, String driver, int driverID, String date) {
        this.id = id;
        this.item = item;
        this.itemID = itemID;
        this.quantity = quantity;
        this.driver = driver;
        this.driverID = driverID;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
