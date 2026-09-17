package com.findash;

import com.findash.repository.DatabaseInitializer;
import com.findash.ui.TransactionsView;
import com.findash.ui.BudgetView;
import com.findash.ui.DashboardView;
import com.findash.ui.AnalyticsView;


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
         BudgetView budgetView =
        new BudgetView();

        TransactionsView transactionsView =
                new TransactionsView(dashboardView, budgetView);
AnalyticsView analyticsView =
        new AnalyticsView();

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
        Tab analyticsTab =
                 new Tab(
                        "Analytics",
                        analyticsView.getView()
        );

        // Prevent tabs from being closed
        dashboardTab.setClosable(false);
        transactionsTab.setClosable(false);
        analyticsTab.setClosable(false);

     

Tab budgetTab =
        new Tab(
                "Budgets",
                budgetView.getView()
        );

TabPane tabPane = new TabPane(
        dashboardTab,
        transactionsTab,
        budgetTab,
        analyticsTab
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