package com.findash.service;

import com.findash.model.Transaction;

import java.util.List;

public class FinanceSummary {

    public double calculateIncome(List<Transaction> transactions) {

        double income = 0;

        for (Transaction transaction : transactions) {

            if (transaction.getAmount() > 0) {
                income += transaction.getAmount();
            }
        }

        return income;
    }

    public double calculateExpenses(List<Transaction> transactions) {

        double expenses = 0;

        for (Transaction transaction : transactions) {

            if (transaction.getAmount() < 0) {
                expenses += Math.abs(transaction.getAmount());
            }
        }

        return expenses;
    }

    public double calculateBalance(List<Transaction> transactions) {

        double balance = 0;

        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }

        return balance;
    }
}