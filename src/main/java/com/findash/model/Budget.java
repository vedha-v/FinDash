package com.findash.model;

public class Budget {

    private int id;
    private String category;
    private double amount;

    public Budget(
            int id,
            String category,
            double amount) {

        this.id = id;
        this.category = category;
        this.amount = amount;
    }

    // Used when creating a new budget
    public Budget(
            String category,
            double amount) {

        this.category = category;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}