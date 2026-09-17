package com.findash.ui;

import com.findash.model.Transaction;
import com.findash.repository.TransactionRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TransactionsView {

    private final TableView<Transaction> table;
    private final TransactionRepository repository;

    private final DatePicker datePicker;
    private final TextField descriptionField;
    private final TextField amountField;
    private final TextField categoryField;

    public TransactionsView() {

        repository = new TransactionRepository();

        table = new TableView<>();

        // -----------------------------
        // Table columns
        // -----------------------------

        TableColumn<Transaction, LocalDate> dateColumn =
                new TableColumn<>("Date");

        TableColumn<Transaction, String> descriptionColumn =
                new TableColumn<>("Description");

        TableColumn<Transaction, String> categoryColumn =
                new TableColumn<>("Category");

        TableColumn<Transaction, Double> amountColumn =
                new TableColumn<>("Amount");

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("date")
        );

        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<>("description")
        );

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category")
        );

        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount")
        );

        table.getColumns().addAll(
                dateColumn,
                descriptionColumn,
                categoryColumn,
                amountColumn
        );

        // -----------------------------
        // Input fields
        // -----------------------------

        datePicker = new DatePicker();

        descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        amountField = new TextField();
        amountField.setPromptText("Amount");

        categoryField = new TextField();
        categoryField.setPromptText("Category");

        Button addButton = new Button("Add Transaction");

        addButton.setOnAction(event -> addTransaction());

        // -----------------------------
        // Layout
        // -----------------------------

        HBox form = new HBox(
                10,
                datePicker,
                descriptionField,
                amountField,
                categoryField,
                addButton
        );

        VBox layout = new VBox(
                15,
                new Label("Transactions"),
                form,
                table
        );

        layout.setPrefWidth(900);

        loadTransactions();

        this.layout = layout;
    }

    private final VBox layout;

    private void addTransaction() {

        LocalDate date = datePicker.getValue();

        String description =
                descriptionField.getText().trim();

        String amountText =
                amountField.getText().trim();

        String category =
                categoryField.getText().trim();

        // -----------------------------
        // Validation
        // -----------------------------

        if (date == null ||
                description.isEmpty() ||
                amountText.isEmpty() ||
                category.isEmpty()) {

            System.out.println(
                    "Please fill in all fields."
            );

            return;
        }

        double amount;

        try {

            amount = Double.parseDouble(amountText);

        } catch (NumberFormatException e) {

            System.out.println(
                    "Amount must be a valid number."
            );

            return;
        }

        // -----------------------------
        // Create transaction
        // -----------------------------

        Transaction transaction =
                new Transaction(
                        date,
                        description,
                        amount,
                        category
                );

        // -----------------------------
        // Save to database
        // -----------------------------

        try {

            repository.save(transaction);

            System.out.println(
                    "Transaction added successfully."
            );

            clearForm();

            loadTransactions();

        } catch (SQLException e) {

            System.out.println(
                    "Failed to save transaction."
            );

            e.printStackTrace();
        }
    }

    private void loadTransactions() {

        try {

            List<Transaction> transactions =
                    repository.findAll();

            ObservableList<Transaction> data =
                    FXCollections.observableArrayList(
                            transactions
                    );

            table.setItems(data);

        } catch (SQLException e) {

            System.out.println(
                    "Failed to load transactions."
            );

            e.printStackTrace();
        }
    }

    private void clearForm() {

        datePicker.setValue(null);
        descriptionField.clear();
        amountField.clear();
        categoryField.clear();
    }

    public VBox getView() {

        return layout;
    }
}