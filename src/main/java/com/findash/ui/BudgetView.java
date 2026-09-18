package com.findash.ui;

import com.findash.model.Budget;
import com.findash.model.Transaction;
import com.findash.repository.BudgetRepository;
import com.findash.repository.TransactionRepository;
import com.findash.service.BudgetService;
import com.findash.service.BudgetStatus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class BudgetView {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetService budgetService;

    private final TableView<BudgetStatus> table;

    private final TextField categoryField;
    private final TextField amountField;

    private final Button addButton;
    private final Button updateButton;
    private final Button deleteButton;

    public BudgetView() {

        budgetRepository = new BudgetRepository();
        transactionRepository = new TransactionRepository();
        budgetService = new BudgetService();

        table = new TableView<>();

   
        TableColumn<BudgetStatus, String> categoryColumn =
                new TableColumn<>("Category");

        TableColumn<BudgetStatus, Double> budgetColumn =
                new TableColumn<>("Budget");

        TableColumn<BudgetStatus, Double> spentColumn =
                new TableColumn<>("Spent");

        TableColumn<BudgetStatus, Double> remainingColumn =
                new TableColumn<>("Remaining");

        TableColumn<BudgetStatus, Double> percentageColumn =
                new TableColumn<>("Used %");

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category")
        );

        budgetColumn.setCellValueFactory(
                new PropertyValueFactory<>("budget")
        );

        spentColumn.setCellValueFactory(
                new PropertyValueFactory<>("spent")
        );

        remainingColumn.setCellValueFactory(
                new PropertyValueFactory<>("remaining")
        );

        percentageColumn.setCellValueFactory(
                new PropertyValueFactory<>("percentageUsed")
        );

        table.getColumns().addAll(
                categoryColumn,
                budgetColumn,
                spentColumn,
                remainingColumn,
                percentageColumn
        );

        categoryField = new TextField();
        categoryField.setPromptText("Category");

        amountField = new TextField();
        amountField.setPromptText("Budget Amount");

    

        addButton =
                new Button("Add Budget");

        updateButton =
                new Button("Update Selected");

        deleteButton =
                new Button("Delete Selected");

        addButton.setOnAction(
                event -> addBudget()
        );

        updateButton.setOnAction(
                event -> updateBudget()
        );

        deleteButton.setOnAction(
                event -> deleteBudget()
        );


        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldStatus, selectedStatus) -> {

                            if (selectedStatus != null) {

                                categoryField.setText(
                                        selectedStatus.getCategory()
                                );

                                amountField.setText(
                                        String.valueOf(
                                                selectedStatus.getBudget()
                                        )
                                );
                            }
                        }
                );

        refresh();
    }

    private void addBudget() {

        String category =
                categoryField.getText().trim();

        String amountText =
                amountField.getText().trim();

        if (category.isEmpty() ||
                amountText.isEmpty()) {

            showError(
                    "Please fill in all fields."
            );

            return;
        }

        double amount;

        try {

            amount =
                    Double.parseDouble(amountText);

        } catch (NumberFormatException e) {

            showError(
                    "Budget amount must be a valid number."
            );

            return;
        }

        if (amount <= 0) {

            showError(
                    "Budget amount must be greater than zero."
            );

            return;
        }

        Budget budget =
                new Budget(
                        category,
                        amount
                );

        try {

            budgetRepository.save(budget);

            clearForm();
            refresh();

            System.out.println(
                    "Budget added successfully."
            );

        } catch (SQLException e) {

            showError(
                    "Could not add budget.\n" +
                    "This category may already have a budget."
            );

            e.printStackTrace();
        }
    }



    private void updateBudget() {

        BudgetStatus selectedStatus =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selectedStatus == null) {

            showError(
                    "Please select a budget to update."
            );

            return;
        }

        String category =
                categoryField.getText().trim();

        String amountText =
                amountField.getText().trim();

        if (category.isEmpty() ||
                amountText.isEmpty()) {

            showError(
                    "Please fill in all fields."
            );

            return;
        }

        double amount;

        try {

            amount =
                    Double.parseDouble(amountText);

        } catch (NumberFormatException e) {

            showError(
                    "Budget amount must be a valid number."
            );

            return;
        }

        if (amount <= 0) {

            showError(
                    "Budget amount must be greater than zero."
            );

            return;
        }

        try {

            // Find the original Budget so we retain its ID
            List<Budget> budgets =
                    budgetRepository.findAll();

            Budget selectedBudget = null;

            for (Budget budget : budgets) {

                if (budget.getCategory()
                        .equalsIgnoreCase(
                                selectedStatus.getCategory()
                        )) {

                    selectedBudget = budget;
                    break;
                }
            }

            if (selectedBudget == null) {

                showError(
                        "Could not find the selected budget."
                );

                return;
            }

            selectedBudget.setCategory(category);
            selectedBudget.setAmount(amount);

            budgetRepository.update(
                    selectedBudget
            );

            clearForm();
            refresh();

            System.out.println(
                    "Budget updated successfully."
            );

        } catch (SQLException e) {

            showError(
                    "Could not update budget."
            );

            e.printStackTrace();
        }
    }


    private void deleteBudget() {

        BudgetStatus selectedStatus =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selectedStatus == null) {

            showError(
                    "Please select a budget to delete."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Budget"
        );

        confirmation.setHeaderText(
                "Delete this budget?"
        );

        confirmation.setContentText(
                selectedStatus.getCategory()
        );

        confirmation.showAndWait()
                .ifPresent(response -> {

                    if (response ==
                            ButtonType.OK) {

                        try {

                            List<Budget> budgets =
                                    budgetRepository.findAll();

                            for (Budget budget : budgets) {

                                if (budget.getCategory()
                                        .equalsIgnoreCase(
                                                selectedStatus.getCategory()
                                        )) {

                                    budgetRepository.delete(
                                            budget.getId()
                                    );

                                    break;
                                }
                            }

                            clearForm();
                            refresh();

                            System.out.println(
                                    "Budget deleted successfully."
                            );

                        } catch (SQLException e) {

                            showError(
                                    "Could not delete budget."
                            );

                            e.printStackTrace();
                        }
                    }
                });
    }


    public void refresh() {

        try {

            List<Budget> budgets =
                    budgetRepository.findAll();

            List<Transaction> transactions =
                    transactionRepository.findAll();

            ObservableList<BudgetStatus> statuses =
                    FXCollections.observableArrayList();

            for (Budget budget : budgets) {

                BudgetStatus status =
                        budgetService.calculateStatus(
                                budget,
                                transactions
                        );

                statuses.add(status);
            }

            table.setItems(statuses);

        } catch (SQLException e) {

            showError(
                    "Could not load budgets."
            );

            e.printStackTrace();
        }
    }

    private void clearForm() {

        categoryField.clear();
        amountField.clear();

        table.getSelectionModel()
                .clearSelection();
    }

    private void showError(String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Budget Error"
        );

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }



   public VBox getView() {

    Label heading =
            new Label("Budget Management");

    heading.setStyle(
            "-fx-font-size: 30px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #292735;"
    );

    Label subtitle =
            new Label(
                    "Set spending limits and track how much of each budget you've used."
            );

    subtitle.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-text-fill: #777481;"
    );

    // Input fields
    categoryField.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10;"
    );

    amountField.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #D6CEFF; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10;"
    );

    categoryField.setPrefWidth(220);
    amountField.setPrefWidth(180);

    // Add button
    addButton.setStyle(
            "-fx-background-color: #B8E0C2; " +
            "-fx-text-fill: #292735; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10 18;"
    );

    // Update button
    updateButton.setStyle(
            "-fx-background-color: #EAE5FF; " +
            "-fx-text-fill: #292735; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10 18;"
    );

    // Delete button
    deleteButton.setStyle(
            "-fx-background-color: #F0EBFF; " +
            "-fx-text-fill: #292735; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10 18;"
    );

    HBox inputRow =
            new HBox(
                    12,
                    categoryField,
                    amountField,
                    addButton
            );

    inputRow.setAlignment(Pos.CENTER_LEFT);

    HBox actionRow =
            new HBox(
                    12,
                    updateButton,
                    deleteButton
            );

    actionRow.setAlignment(Pos.CENTER_LEFT);

    // Style the table
    table.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #EAE5FF; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10;"
    );

    table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY
    );

    VBox layout =
            new VBox(
                    8,
                    heading,
                    subtitle,
                    inputRow,
                    table,
                    actionRow
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