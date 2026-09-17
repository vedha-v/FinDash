package com.findash;

import com.findash.repository.DatabaseInitializer;
import com.findash.ui.TransactionsView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        try {

            DatabaseInitializer.initialize();

            System.out.println(
                    "Database initialized successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Database initialization failed."
            );

            e.printStackTrace();
        }

        TransactionsView transactionsView =
                new TransactionsView();

        Scene scene =
                new Scene(
                        transactionsView.getView(),
                        900,
                        600
                );

        stage.setTitle("FinDash - Transactions");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}