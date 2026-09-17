package com.findash.service;

import com.findash.model.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAnalytics {

    public Map<String, Double> calculateSpendingByCategory(
            List<Transaction> transactions) {

        Map<String, Double> spending =
                new HashMap<>();

        for (Transaction transaction : transactions) {

            if (transaction.getAmount() < 0) {

                String category =
                        transaction.getCategory();

                double amount =
                        Math.abs(transaction.getAmount());

                spending.put(
                        category,
                        spending.getOrDefault(category, 0.0)
                                + amount
                );
            }
        }

        return spending;
    }
}