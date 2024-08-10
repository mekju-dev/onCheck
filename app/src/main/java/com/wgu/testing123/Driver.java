package com.wgu.testing123;

public class Driver {
    int id;
    String firstName;
    String lastName;
    String phone;
    int workToday;
    public Driver (){

    }
    public Driver(int id, String firstName, String lastName, String phone, int workToday) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.workToday = workToday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getWorkToday() {
        return workToday;
    }

    public void setWorkToday(int workToday) {
        this.workToday = workToday;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
