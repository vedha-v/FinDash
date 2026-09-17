package com.findash.ui;

import com.findash.model.Transaction;
import com.findash.repository.TransactionRepository;
import com.findash.service.TransactionFilter;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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

    private final DashboardView dashboardView;
    private final BudgetView budgetView;
    private final AnalyticsView analyticsView;

    // Transaction input fields
    private final DatePicker datePicker;
    private final TextField descriptionField;
    private final TextField amountField;
    private final TextField categoryField;

    // Transaction buttons
    private final Button addButton;
    private final Button updateButton;
    private final Button deleteButton;

    // Filtering
    private final TransactionFilter transactionFilter;
    private final TextField searchField;
    private final ComboBox<String> categoryFilter;
    private final DatePicker fromDateFilter;
    private final DatePicker toDateFilter;


    public TransactionsView(
            DashboardView dashboardView,
            BudgetView budgetView,
            AnalyticsView analyticsView) {

        this.dashboardView = dashboardView;
        this.budgetView = budgetView;
        this.analyticsView = analyticsView;

        repository =
                new TransactionRepository();

        transactionFilter =
                new TransactionFilter();


        // --------------------------------
        // Filter controls
        // --------------------------------

        searchField =
                new TextField();

        searchField.setPromptText(
                "Search description..."
        );

        categoryFilter =
                new ComboBox<>();

        categoryFilter.getItems().add(
                "All"
        );

        categoryFilter.setValue(
                "All"
        );

        fromDateFilter =
                new DatePicker();

        fromDateFilter.setPromptText(
                "From date"
        );

        toDateFilter =
                new DatePicker();

        toDateFilter.setPromptText(
                "To date"
        );


        // --------------------------------
        // Table
        // --------------------------------

        table =
                new TableView<>();


        // --------------------------------
        // Table columns
        // --------------------------------

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


        // --------------------------------
        // Input fields
        // --------------------------------

        datePicker =
                new DatePicker();

        descriptionField =
                new TextField();

        descriptionField.setPromptText(
                "Description"
        );

        amountField =
                new TextField();

        amountField.setPromptText(
                "Amount"
        );

        categoryField =
                new TextField();

        categoryField.setPromptText(
                "Category"
        );


        // --------------------------------
        // Buttons
        // --------------------------------

        addButton =
                new Button("Add Transaction");

        updateButton =
                new Button("Update Selected");

        deleteButton =
                new Button("Delete Selected");


        addButton.setOnAction(
                event -> addTransaction()
        );

        updateButton.setOnAction(
                event -> updateTransaction()
        );

        deleteButton.setOnAction(
                event -> deleteTransaction()
        );


        // --------------------------------
        // Filter listeners
        // --------------------------------

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        loadTransactions()
        );

        categoryFilter.setOnAction(
                event -> loadTransactions()
        );

        fromDateFilter.setOnAction(
                event -> loadTransactions()
        );

        toDateFilter.setOnAction(
                event -> loadTransactions()
        );


        // --------------------------------
        // Table selection
        // --------------------------------

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         oldTransaction,
                         selectedTransaction) -> {

                            if (selectedTransaction != null) {

                                datePicker.setValue(
                                        selectedTransaction
                                                .getDate()
                                );

                                descriptionField.setText(
                                        selectedTransaction
                                                .getDescription()
                                );

                                amountField.setText(
                                        String.valueOf(
                                                selectedTransaction
                                                        .getAmount()
                                        )
                                );

                                categoryField.setText(
                                        selectedTransaction
                                                .getCategory()
                                );
                            }
                        }
                );


        // --------------------------------
        // Initial data loading
        // --------------------------------

        loadCategoryFilter();
        loadTransactions();
    }


    // --------------------------------
    // Add transaction
    // --------------------------------

    private void addTransaction() {

        LocalDate date =
                datePicker.getValue();

        String description =
                descriptionField.getText().trim();

        String amountText =
                amountField.getText().trim();

        String category =
                categoryField.getText().trim();


        if (date == null ||
                description.isEmpty() ||
                amountText.isEmpty() ||
                category.isEmpty()) {

            showError(
                    "Please fill in all fields."
            );

            return;
        }


        double amount;

        try {

            amount =
                    Double.parseDouble(
                            amountText
                    );

        } catch (NumberFormatException e) {

            showError(
                    "Amount must be a valid number."
            );

            return;
        }


        if (amount == 0) {

            showError(
                    "Amount cannot be zero."
            );

            return;
        }


        Transaction transaction =
                new Transaction(
                        date,
                        description,
                        amount,
                        category
                );


        try {

            repository.save(transaction);

            clearForm();

            loadCategoryFilter();
            loadTransactions();

            dashboardView.refresh();
            budgetView.refresh();
            analyticsView.refresh();


            System.out.println(
                    "Transaction added successfully."
            );

        } catch (SQLException e) {

            showError(
                    "Failed to save transaction."
            );

            e.printStackTrace();
        }
    }


    // --------------------------------
    // Update transaction
    // --------------------------------

    private void updateTransaction() {

        Transaction selectedTransaction =
                table.getSelectionModel()
                        .getSelectedItem();


        if (selectedTransaction == null) {

            showError(
                    "Please select a transaction to update."
            );

            return;
        }


        LocalDate date =
                datePicker.getValue();

        String description =
                descriptionField.getText().trim();

        String amountText =
                amountField.getText().trim();

        String category =
                categoryField.getText().trim();


        if (date == null ||
                description.isEmpty() ||
                amountText.isEmpty() ||
                category.isEmpty()) {

            showError(
                    "Please fill in all fields."
            );

            return;
        }


        double amount;

        try {

            amount =
                    Double.parseDouble(
                            amountText
                    );

        } catch (NumberFormatException e) {

            showError(
                    "Amount must be a valid number."
            );

            return;
        }


        if (amount == 0) {

            showError(
                    "Amount cannot be zero."
            );

            return;
        }


        selectedTransaction.setDate(date);

        selectedTransaction.setDescription(
                description
        );

        selectedTransaction.setAmount(
                amount
        );

        selectedTransaction.setCategory(
                category
        );


        try {

            repository.update(
                    selectedTransaction
            );

            clearForm();

            loadCategoryFilter();
            loadTransactions();

            dashboardView.refresh();
            budgetView.refresh();
            analyticsView.refresh();


            System.out.println(
                    "Transaction updated successfully."
            );

        } catch (SQLException e) {

            showError(
                    "Failed to update transaction."
            );

            e.printStackTrace();
        }
    }


    // --------------------------------
    // Delete transaction
    // --------------------------------

    private void deleteTransaction() {

        Transaction selectedTransaction =
                table.getSelectionModel()
                        .getSelectedItem();


        if (selectedTransaction == null) {

            showError(
                    "Please select a transaction to delete."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Transaction"
        );

        confirmation.setHeaderText(
                "Delete this transaction?"
        );

        confirmation.setContentText(
                selectedTransaction.getDescription()
        );


        confirmation.showAndWait()
                .ifPresent(response -> {

                    if (response ==
                            ButtonType.OK) {

                        try {

                            repository.delete(
                                    selectedTransaction
                                            .getId()
                            );

                            clearForm();

                            loadCategoryFilter();
                            loadTransactions();

                            dashboardView.refresh();
                            budgetView.refresh();
                            analyticsView.refresh();


                            System.out.println(
                                    "Transaction deleted successfully."
                            );

                        } catch (SQLException e) {

                            showError(
                                    "Failed to delete transaction."
                            );

                            e.printStackTrace();
                        }
                    }
                });
    }


    // --------------------------------
    // Load transactions
    // --------------------------------

    private void loadTransactions() {

        try {

            List<Transaction> transactions =
                    repository.findAll();

            List<Transaction> filtered =
                    transactionFilter.filter(
                            transactions,
                            searchField.getText(),
                            categoryFilter.getValue(),
                            fromDateFilter.getValue(),
                            toDateFilter.getValue()
                    );

            table.setItems(
                    FXCollections.observableArrayList(
                            filtered
                    )
            );

        } catch (SQLException e) {

            System.out.println(
                    "Failed to load transactions."
            );

            e.printStackTrace();
        }
    }


    // --------------------------------
    // Load category filter
    // --------------------------------

    private void loadCategoryFilter() {

        try {

            List<Transaction> transactions =
                    repository.findAll();

            String currentCategory =
                    categoryFilter.getValue();


            categoryFilter.getItems().clear();

            categoryFilter.getItems().add(
                    "All"
            );


            for (Transaction transaction :
                    transactions) {

                String category =
                        transaction.getCategory();

                if (!categoryFilter
                        .getItems()
                        .contains(category)) {

                    categoryFilter
                            .getItems()
                            .add(category);
                }
            }


            if (currentCategory != null &&
                    categoryFilter
                            .getItems()
                            .contains(currentCategory)) {

                categoryFilter.setValue(
                        currentCategory
                );

            } else {

                categoryFilter.setValue(
                        "All"
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to load categories."
            );

            e.printStackTrace();
        }
    }


    // --------------------------------
    // Clear form
    // --------------------------------

    private void clearForm() {

        datePicker.setValue(null);

        descriptionField.clear();

        amountField.clear();

        categoryField.clear();

        table.getSelectionModel()
                .clearSelection();
    }


    // --------------------------------
    // Error dialog
    // --------------------------------

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Transaction Error"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


    // --------------------------------
    // UI
    // --------------------------------

    public VBox getView() {

        Label heading =
                new Label(
                        "Transaction Management"
                );

        heading.setStyle(
                "-fx-font-size: 28px; " +
                "-fx-font-weight: bold;"
        );


        // Filter row

        HBox filterRow =
                new HBox(
                        10,
                        searchField,
                        categoryFilter,
                        fromDateFilter,
                        toDateFilter
                );


        // Transaction input row

        HBox inputRow =
                new HBox(
                        10,
                        datePicker,
                        descriptionField,
                        amountField,
                        categoryField
                );


        // Action buttons

        HBox actionRow =
                new HBox(
                        10,
                        addButton,
                        updateButton,
                        deleteButton
                );


        VBox layout =
                new VBox(
                        20,
                        heading,
                        filterRow,
                        inputRow,
                        actionRow,
                        table
                );


        layout.setPadding(
                new Insets(30)
        );


        return layout;
    }
}