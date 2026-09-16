package com.findash;
import com.findash.repository.DatabaseInitializer;
import com.findash.model.Transaction;
import com.findash.repository.TransactionRepository;
import java.sql.SQLException ;
import java.util.List;
import java.time.LocalDate;

public class Main extends javafx.application.Application {

    @Override
    public void start(javafx.stage.Stage stage) {

        try {
    DatabaseInitializer.initialize();
    System.out.println("Database initialized successfully.");
} catch (Exception e) {
    System.out.println("Database initialization failed.");
    e.printStackTrace();
}

        Transaction transaction = new Transaction(
        LocalDate.now(),
        "Grocery Shopping",
        -750.50,
        "Food"
);

        TransactionRepository repository = new TransactionRepository();

        try {
    List<Transaction> transactions =
            repository.findAll();

    System.out.println("Transactions in database:");

    for (Transaction t : transactions) {
        System.out.println(
                t.getDate()
                + " | "
                + t.getDescription()
                + " | "
                + t.getAmount()
                + " | "
                + t.getCategory()
        );}
} catch (SQLException e) {
    System.out.println("Failed to save transaction.");
    e.printStackTrace();
}

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