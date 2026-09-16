package com.findash;

import com.findash.model.Transaction;

import java.time.LocalDate;

public class Main extends javafx.application.Application {

    @Override
    public void start(javafx.stage.Stage stage) {

        Transaction transaction = new Transaction(
                1,
                LocalDate.now(),
                "Grocery Shopping",
                -750.50,
                "Food"
        );

        System.out.println("Transaction: "
                + transaction.getDescription());

        System.out.println("Amount: "
                + transaction.getAmount());

        javafx.scene.control.Label title =
                new javafx.scene.control.Label("FinDash");

        javafx.scene.layout.StackPane root =
                new javafx.scene.layout.StackPane(title);

        javafx.scene.Scene scene =
                new javafx.scene.Scene(root, 900, 600);

        stage.setTitle("FinDash - Finance Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}