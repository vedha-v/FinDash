package com.findash;

import com.findash.repository.DatabaseInitializer;
import com.findash.ui.TransactionsView;
import com.findash.ui.DashboardView;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
          // Create our two main screens
        DashboardView dashboardView =
                new DashboardView();

        TransactionsView transactionsView =
                new TransactionsView();

        // Create tabs
        Tab dashboardTab =
                new Tab(
                        "Dashboard",
                        dashboardView.getView()
                );

        Tab transactionsTab =
                new Tab(
                        "Transactions",
                        transactionsView.getView()
                );

        // Prevent tabs from being closed
        dashboardTab.setClosable(false);
        transactionsTab.setClosable(false);

        TabPane tabPane = new TabPane(
                dashboardTab,
                transactionsTab
        );

        

         Scene scene =
                new Scene(
                        tabPane,
                        900,
                        600
                );

        stage.setTitle("FinDash - Finance Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}