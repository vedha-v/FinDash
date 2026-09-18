# FinDash

## Personal Finance Dashboard

FinDash is a Java-based personal finance management application designed to help users record transactions, manage spending budgets, and understand their financial activity through visual analytics.

The application provides a desktop interface built using JavaFX and stores financial data locally using SQLite and JDBC.


## Features

### 1. Transaction Management

Users can:

Add income and expense transactions
1.Enter transaction date, description, amount and category
2.Edit existing transactions
3.Delete transactions
4.Search transactions by description
5.Filter transactions by category
6.Filter transactions by date range

### 2. Financial Dashboard

The dashboard provides an overview of:

- Total income
- Total expenses
- Current balance

The dashboard is automatically refreshed when transaction data changes.

### 3. Budget Management

Users can:

1.Create category based budgets
2.Update existing budgets
3.Delete budgets
4.View total budget amount
5.View amount spent
6.View remaining budget
7.View percentage of budget used

### 4. Category Analytics

FinDash analyzes expenses by category and presents the results using:

- Category spending table
- Bar chart visualization


### 5. Persistent Storage

Financial information is stored locally using:

- SQLite database
- JDBC
- SQL tables for transactions and budgets

Data remains available when the application is restarted.



## Technologies Used

 Java 17(minimum) - Application development 
 JavaFX - Graphical user interface (GUI)
 Maven - Project and dependency management 
 SQLite - Local database 
 JDBC - Database connectivity 
 Git - Version control 

---

## Project Structure

```text
src/
└── main/
    ├── java/
    │   └── com/
    │       └── findash/
    │           ├── Main.java
    │           ├── model/
    │           ├── repository/
    │           ├── service/
    │           ├── ui/
    │           └── exception/
    │
    └── resources/