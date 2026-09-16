package com.findash.repository;

import com.findash.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}