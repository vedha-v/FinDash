package com.findash.ui;

import com.findash.model.Transaction;
import com.findash.repository.TransactionRepository;
import com.findash.service.TransactionFilter;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    private final DatePicker datePicker;
    private final TextField descriptionField;
    private final TextField amountField;
    private final TextField categoryField;

    private final Button addButton;
    private final Button updateButton;
    private final Button deleteButton;

    
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

        table =
                new TableView<>();


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


        loadCategoryFilter();
        loadTransactions();
    }



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


    private void clearForm() {

        datePicker.setValue(null);

        descriptionField.clear();

        amountField.clear();

        categoryField.clear();

        table.getSelectionModel()
                .clearSelection();
    }



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


    public VBox getView() {

    Label heading =
            new Label("Transaction Management");

    heading.setStyle(
            "-fx-font-size: 30px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #292735;"
    );

    Label subtitle =
            new Label(
                    "Add, edit, delete and filter your financial transactions."
            );

    subtitle.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-text-fill: #777481;"
    );

    // -------------------------
    // Filter section
    // -------------------------

    Label filterTitle =
            new Label("Filter Transactions");

    filterTitle.setStyle(
            "-fx-font-size: 17px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #292735;"
    );

    searchField.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 9;"
    );

    categoryFilter.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8;"
    );

    fromDateFilter.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8;"
    );

    toDateFilter.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8;"
    );

    searchField.setPrefWidth(220);
    categoryFilter.setPrefWidth(150);
    fromDateFilter.setPrefWidth(150);
    toDateFilter.setPrefWidth(150);

    HBox filterRow =
            new HBox(
                    10,
                    searchField,
                    categoryFilter,
                    fromDateFilter,
                    toDateFilter
            );

    filterRow.setAlignment(Pos.CENTER_LEFT);

    VBox filterSection =
            new VBox(
                    10,
                    filterTitle,
                    filterRow
            );

    filterSection.setPadding(
            new Insets(15)
    );

    filterSection.setStyle(
            "-fx-background-color: #F0EBFF; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 12;"
    );

    // -------------------------
    // Transaction input section
    // -------------------------

    Label detailsTitle =
            new Label("Transaction Details");

    detailsTitle.setStyle(
            "-fx-font-size: 17px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #292735;"
    );

    datePicker.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8;"
    );

    descriptionField.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 9;"
    );

    amountField.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 9;"
    );

    categoryField.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 9;"
    );

    // -------------------------
    // Buttons
    // -------------------------

    addButton.setStyle(
            "-fx-background-color: #B8E0C2; " +
            "-fx-text-fill: #292735; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10 18;"
    );

    updateButton.setStyle(
            "-fx-background-color: #EAE5FF; " +
            "-fx-text-fill: #292735; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10 18;"
    );

    deleteButton.setStyle(
            "-fx-background-color: #F0EBFF; " +
            "-fx-text-fill: #292735; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10 18;"
    );

    HBox inputRow =
            new HBox(
                    10,
                    datePicker,
                    descriptionField,
                    amountField,
                    categoryField
            );

    inputRow.setAlignment(Pos.CENTER_LEFT);

    HBox buttonRow =
            new HBox(
                    10,
                    addButton,
                    updateButton,
                    deleteButton
            );

    buttonRow.setAlignment(Pos.CENTER_LEFT);

    VBox detailsSection =
            new VBox(
                    10,
                    detailsTitle,
                    inputRow,
                    buttonRow
            );

    detailsSection.setPadding(
            new Insets(15)
    );

    detailsSection.setStyle(
            "-fx-background-color: #E4F3E7; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #B8E0C2; " +
            "-fx-border-radius: 12;"
    );

    // -------------------------
    // Transaction table
    // -------------------------

    table.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #EAE5FF; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10;"
    );

    table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY
    );

    // -------------------------
    // Final layout
    // -------------------------

    VBox layout =
            new VBox(
                    12,
                    heading,
                    subtitle,
                    filterSection,
                    detailsSection,
                    table
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