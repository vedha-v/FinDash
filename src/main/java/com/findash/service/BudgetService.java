package com.findash.service;

import com.findash.model.Budget;
import com.findash.model.Transaction;

import java.util.List;

public class BudgetService {

    public BudgetStatus calculateStatus(
            Budget budget,
            List<Transaction> transactions) {

        double spent = 0;

        for (Transaction transaction : transactions) {

            if (transaction.getCategory()
                    .equalsIgnoreCase(
                            budget.getCategory()
                    )
                    && transaction.getAmount() < 0) {

                spent += Math.abs(
                        transaction.getAmount()
                );
            }
        }

        return new BudgetStatus(
                budget.getCategory(),
                budget.getAmount(),
                spent
        );
    }
}