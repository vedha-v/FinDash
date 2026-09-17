package com.findash.service;

public class BudgetStatus {

    private final String category;
    private final double budget;
    private final double spent;

    public BudgetStatus(
            String category,
            double budget,
            double spent) {

        this.category = category;
        this.budget = budget;
        this.spent = spent;
    }

    public String getCategory() {
        return category;
    }

    public double getBudget() {
        return budget;
    }

    public double getSpent() {
        return spent;
    }

    public double getRemaining() {
        return budget - spent;
    }

    public double getPercentageUsed() {

        if (budget <= 0) {
            return 0;
        }

        return (spent / budget) * 100;
    }
}