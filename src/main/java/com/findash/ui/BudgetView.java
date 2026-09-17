package com.findash.ui;

import com.findash.model.Budget;
import com.findash.repository.BudgetRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    private final BudgetRepository repository;

    private final TableView<Budget> table;

    private final TextField categoryField;
    private final TextField amountField;

    private final Button addButton;
    private final Button updateButton;
    private final Button deleteButton;

    public BudgetView() {

        repository = new BudgetRepository();

        table = new TableView<>();

        // --------------------------------
        // Table columns
        // --------------------------------

        TableColumn<Budget, String> categoryColumn =
                new TableColumn<>("Category");

        TableColumn<Budget, Double> amountColumn =
                new TableColumn<>("Budget Amount");

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category")
        );

        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount")
        );

        table.getColumns().addAll(
                categoryColumn,
                amountColumn
        );

        // --------------------------------
        // Input fields
        // --------------------------------

        categoryField = new TextField();
        categoryField.setPromptText("Category");

        amountField = new TextField();
        amountField.setPromptText("Budget Amount");

        // --------------------------------
        // Buttons
        // --------------------------------

        addButton =
                new Button("Add Budget");

        updateButton =
                new Button("Update Selected");

        deleteButton =
                new Button("Delete Selected");

        // --------------------------------
        // Button actions
        // --------------------------------

        addButton.setOnAction(
                event -> addBudget()
        );

        updateButton.setOnAction(
                event -> updateBudget()
        );

        deleteButton.setOnAction(
                event -> deleteBudget()
        );

        // --------------------------------
        // Table selection
        // --------------------------------

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldBudget, selectedBudget) -> {

                            if (selectedBudget != null) {

                                categoryField.setText(
                                        selectedBudget.getCategory()
                                );

                                amountField.setText(
                                        String.valueOf(
                                                selectedBudget.getAmount()
                                        )
                                );
                            }
                        }
                );

        loadBudgets();
    }

    // --------------------------------
    // Add budget
    // --------------------------------

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

            repository.save(budget);

            System.out.println(
                    "Budget added successfully."
            );

            clearForm();
            loadBudgets();

        } catch (SQLException e) {

            showError(
                    "Could not add budget.\n" +
                    "Make sure this category does not already have a budget."
            );

            e.printStackTrace();
        }
    }

    // --------------------------------
    // Update budget
    // --------------------------------

    private void updateBudget() {

        Budget selectedBudget =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selectedBudget == null) {

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

        selectedBudget.setCategory(category);
        selectedBudget.setAmount(amount);

        try {

            repository.update(
                    selectedBudget
            );

            System.out.println(
                    "Budget updated successfully."
            );

            clearForm();
            loadBudgets();

        } catch (SQLException e) {

            showError(
                    "Could not update budget."
            );

            e.printStackTrace();
        }
    }

    // --------------------------------
    // Delete budget
    // --------------------------------

    private void deleteBudget() {

        Budget selectedBudget =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selectedBudget == null) {

            showError(
                    "Please select a budget to delete."
            );

            return;
        }

        try {

            repository.delete(
                    selectedBudget.getId()
            );

            System.out.println(
                    "Budget deleted successfully."
            );

            clearForm();
            loadBudgets();

        } catch (SQLException e) {

            showError(
                    "Could not delete budget."
            );

            e.printStackTrace();
        }
    }

    // --------------------------------
    // Load budgets
    // --------------------------------

    private void loadBudgets() {

        try {

            List<Budget> budgets =
                    repository.findAll();

            ObservableList<Budget> data =
                    FXCollections.observableArrayList(
                            budgets
                    );

            table.setItems(data);

        } catch (SQLException e) {

            showError(
                    "Could not load budgets."
            );

            e.printStackTrace();
        }
    }

    // --------------------------------
    // Clear form
    // --------------------------------

    private void clearForm() {

        categoryField.clear();
        amountField.clear();

        table.getSelectionModel()
                .clearSelection();
    }

    // --------------------------------
    // Error dialog
    // --------------------------------

    private void showError(String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle("Budget Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // --------------------------------
    // UI
    // --------------------------------

    public VBox getView() {

        Label heading =
                new Label("Budget Management");

        heading.setStyle(
                "-fx-font-size: 28px; " +
                "-fx-font-weight: bold;"
        );

        HBox inputRow =
                new HBox(
                        10,
                        categoryField,
                        amountField,
                        addButton
                );

        HBox actionRow =
                new HBox(
                        10,
                        updateButton,
                        deleteButton
                );

        VBox layout =
                new VBox(
                        20,
                        heading,
                        inputRow,
                        table,
                        actionRow
                );

        layout.setPadding(
                new Insets(30)
        );

        return layout;
    }
}