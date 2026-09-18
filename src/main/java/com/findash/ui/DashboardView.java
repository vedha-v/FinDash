package com.findash.ui;

import com.findash.model.Transaction;
import com.findash.repository.TransactionRepository;
import com.findash.service.FinanceSummary;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class DashboardView {

    private final TransactionRepository repository;
    private final FinanceSummary summary;

    private final Label incomeValue;
    private final Label expenseValue;
    private final Label balanceValue;

    public DashboardView() {

        repository = new TransactionRepository();
        summary = new FinanceSummary();

        incomeValue = new Label("₹0.00");
        expenseValue = new Label("₹0.00");
        balanceValue = new Label("₹0.00");

        refresh();
    }

    public void refresh() {

        try {
            List<Transaction> transactions = repository.findAll();

            double income =
                    summary.calculateIncome(transactions);

            double expenses =
                    summary.calculateExpenses(transactions);

            double balance =
                    summary.calculateBalance(transactions);

            incomeValue.setText(
                    String.format("₹%.2f", income)
            );

            expenseValue.setText(
                    String.format("₹%.2f", expenses)
            );

            balanceValue.setText(
                    String.format("₹%.2f", balance)
            );

        } catch (SQLException e) {

            System.out.println(
                    "Failed to load financial summary."
            );

            e.printStackTrace();
        }
    }

    private VBox createCard(
            String title,
            Label value) {

        Label titleLabel = new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 15px; " +
                "-fx-text-fill: #777481;"
        );

        value.setStyle(
                "-fx-font-size: 26px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #292735;"
        );

        VBox card = new VBox(
                10,
                titleLabel,
                value
        );

        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(240);
        card.setPrefHeight(125);

        card.setStyle(
                "-fx-background-color: #FFFFFF; " +
                "-fx-background-radius: 14; " +
                "-fx-border-color: #EAE5FF; " +
                "-fx-border-radius: 14;"
        );

        return card;
    }

    public VBox getView() {

        Label heading =
                new Label("Financial Dashboard");

        heading.setStyle(
                "-fx-font-size: 30px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #292735;"
        );

        Label subtitle =
                new Label(
                        "Overview of your income, expenses and current balance."
                );

        subtitle.setStyle(
                "-fx-font-size: 15px; " +
                "-fx-text-fill: #777481;"
        );

        VBox incomeCard =
                createCard(
                        "Total Income",
                        incomeValue
                );

        VBox expenseCard =
                createCard(
                        "Total Expenses",
                        expenseValue
                );

        VBox balanceCard =
                createCard(
                        "Current Balance",
                        balanceValue
                );

        // Pastel green for income
        incomeCard.setStyle(
                "-fx-background-color: #E4F3E7; " +
                "-fx-background-radius: 14; " +
                "-fx-border-color: #B8E0C2; " +
                "-fx-border-radius: 14;"
        );

        // Soft lavender for expenses
        expenseCard.setStyle(
                "-fx-background-color: #EAE5FF; " +
                "-fx-background-radius: 14; " +
                "-fx-border-color: #D6CEFF; " +
                "-fx-border-radius: 14;"
        );

        // Slightly deeper lavender for balance
        balanceCard.setStyle(
                "-fx-background-color: #F0EBFF; " +
                "-fx-background-radius: 14; " +
                "-fx-border-color: #D6CEFF; " +
                "-fx-border-radius: 14;"
        );

        HBox cards =
                new HBox(
                        20,
                        incomeCard,
                        expenseCard,
                        balanceCard
                );

        cards.setAlignment(Pos.CENTER_LEFT);

        VBox layout =
                new VBox(
                        8,
                        heading,
                        subtitle,
                        cards
                );

        layout.setPadding(
                new Insets(30)
        );

        layout.setSpacing(18);

        layout.setStyle(
                "-fx-background-color: #F7F5FA;"
        );

        return layout;
    }
}