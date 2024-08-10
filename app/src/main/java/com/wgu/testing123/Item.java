package com.wgu.testing123;

import java.io.Serializable;

public class Item implements Serializable {
    int ID;
    String item;
    String description;
    int quantity;


    public Item(){

    }

    public Item(int ID, String item, String description, int quantity) {
        this.ID = ID;
        this.item = item;
        this.description = description;
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return item;
    }
}
