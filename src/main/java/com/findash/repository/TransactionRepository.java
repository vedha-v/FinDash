package com.findash.repository;

import com.findash.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void save(Transaction transaction) throws SQLException {

        String sql = """
                INSERT INTO transactions
                (date, description, amount, category)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, transaction.getDate().toString());
            statement.setString(2, transaction.getDescription());
            statement.setDouble(3, transaction.getAmount());
            statement.setString(4, transaction.getCategory());

            statement.executeUpdate();
        }
    }

    public List<Transaction> findAll() throws SQLException {

        List<Transaction> transactions = new ArrayList<>();

        String sql = """
                SELECT id, date, description, amount, category
                FROM transactions
                ORDER BY date DESC
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int id = resultSet.getInt("id");

                LocalDate date = LocalDate.parse(
                        resultSet.getString("date")
                );

                String description =
                        resultSet.getString("description");

                double amount =
                        resultSet.getDouble("amount");

                String category =
                        resultSet.getString("category");

                Transaction transaction = new Transaction(
                        id,
                        date,
                        description,
                        amount,
                        category
                );

                transactions.add(transaction);
            }
        }

        return transactions;
    }
}