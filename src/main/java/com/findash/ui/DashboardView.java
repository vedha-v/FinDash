package com.findash.ui;

import com.findash.model.Transaction;
import com.findash.repository.TransactionRepository;
import com.findash.service.FinanceSummary;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class DashboardView {

    private final TransactionRepository repository;
    private final FinanceSummary summary;

    private final Label incomeLabel;
    private final Label expenseLabel;
    private final Label balanceLabel;

    public DashboardView() {

        repository = new TransactionRepository();
        summary = new FinanceSummary();

        incomeLabel = new Label("Income: ₹0.00");
        expenseLabel = new Label("Expenses: ₹0.00");
        balanceLabel = new Label("Balance: ₹0.00");

        loadSummary();
    }

    private void loadSummary() {

        try {

            List<Transaction> transactions =
                    repository.findAll();

            double income =
                    summary.calculateIncome(transactions);

            double expenses =
                    summary.calculateExpenses(transactions);

            double balance =
                    summary.calculateBalance(transactions);

            incomeLabel.setText(
                    String.format("Income: ₹%.2f", income)
            );

            expenseLabel.setText(
                    String.format("Expenses: ₹%.2f", expenses)
            );

            balanceLabel.setText(
                    String.format("Balance: ₹%.2f", balance)
            );

        } catch (SQLException e) {

            System.out.println(
                    "Failed to load financial summary."
            );

            e.printStackTrace();
        }
    }

    public VBox getView() {

        HBox summaryCards = new HBox(
                30,
                incomeLabel,
                expenseLabel,
                balanceLabel
        );

        VBox layout = new VBox(
                20,
                new Label("Financial Summary"),
                summaryCards
        );

        return layout;
    }
}