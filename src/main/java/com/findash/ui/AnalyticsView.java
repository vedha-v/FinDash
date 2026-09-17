package com.findash.ui;

import com.findash.model.Transaction;
import com.findash.repository.TransactionRepository;
import com.findash.service.CategoryAnalytics;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AnalyticsView {

    private final TransactionRepository repository;
    private final CategoryAnalytics analytics;

    private final TableView<CategorySpending> table;

    public AnalyticsView() {

        repository = new TransactionRepository();
        analytics = new CategoryAnalytics();

        table = new TableView<>();

        createColumns();
        refresh();
    }

    private void createColumns() {

        TableColumn<CategorySpending, String> categoryColumn =
                new TableColumn<>("Category");

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category")
        );

        TableColumn<CategorySpending, Double> spendingColumn =
                new TableColumn<>("Total Spending");

        spendingColumn.setCellValueFactory(
                new PropertyValueFactory<>("spending")
        );

        table.getColumns().addAll(
                categoryColumn,
                spendingColumn
        );

        categoryColumn.setPrefWidth(300);
        spendingColumn.setPrefWidth(300);
    }

    public void refresh() {

        try {

            List<Transaction> transactions =
                    repository.findAll();

            Map<String, Double> spending =
                    analytics.calculateSpendingByCategory(
                            transactions
                    );

            table.setItems(
                    FXCollections.observableArrayList()
            );

            for (Map.Entry<String, Double> entry :
                    spending.entrySet()) {

                table.getItems().add(
                        new CategorySpending(
                                entry.getKey(),
                                entry.getValue()
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to load category analytics."
            );

            e.printStackTrace();
        }
    }

    public VBox getView() {

        Label heading =
                new Label("Category Analytics");

        heading.setStyle(
                "-fx-font-size: 28px; " +
                "-fx-font-weight: bold;"
        );

        Label description =
                new Label(
                        "See how your expenses are distributed across categories."
                );

        description.setStyle(
                "-fx-font-size: 15px;"
        );

        VBox layout =
                new VBox(
                        15,
                        heading,
                        description,
                        table
                );

        layout.setPadding(
                new Insets(30)
        );

        return layout;
    }

    public static class CategorySpending {

        private final String category;
        private final double spending;

        public CategorySpending(
                String category,
                double spending) {

            this.category = category;
            this.spending = spending;
        }

        public String getCategory() {
            return category;
        }

        public double getSpending() {
            return spending;
        }
    }
}