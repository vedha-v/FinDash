package com.findash.repository;

import com.findash.model.Budget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BudgetRepository {

    public void save(Budget budget) throws SQLException {

        String sql = """
                INSERT INTO budgets
                (category, amount)
                VALUES (?, ?)
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    budget.getCategory()
            );

            statement.setDouble(
                    2,
                    budget.getAmount()
            );

            statement.executeUpdate();
        }
    }

    public List<Budget> findAll() throws SQLException {

        List<Budget> budgets = new ArrayList<>();

        String sql = """
                SELECT id, category, amount
                FROM budgets
                ORDER BY category
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                int id =
                        resultSet.getInt("id");

                String category =
                        resultSet.getString("category");

                double amount =
                        resultSet.getDouble("amount");

                Budget budget =
                        new Budget(
                                id,
                                category,
                                amount
                        );

                budgets.add(budget);
            }
        }

        return budgets;
    }

    public void update(Budget budget) throws SQLException {

        String sql = """
                UPDATE budgets
                SET category = ?, amount = ?
                WHERE id = ?
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    budget.getCategory()
            );

            statement.setDouble(
                    2,
                    budget.getAmount()
            );

            statement.setInt(
                    3,
                    budget.getId()
            );

            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {

        String sql = """
                DELETE FROM budgets
                WHERE id = ?
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();
        }
    }
}