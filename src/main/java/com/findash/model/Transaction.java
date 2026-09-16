package com.findash.model;

import java.time.LocalDate;

public class Transaction {

    private int id;
    private LocalDate date;
    private String description;
    private double amount;
    private String category;

    public Transaction(
            int id,
            LocalDate date,
            String description,
            double amount,
            String category) {

        this.id = id;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }
    public Transaction(
        LocalDate date,
        String description,
        double amount,
        String category) {

    this.date = date;
    this.description = description;
    this.amount = amount;
    this.category = category;
}

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}