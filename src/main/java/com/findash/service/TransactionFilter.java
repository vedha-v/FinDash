package com.findash.service;

import com.findash.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionFilter {

    public List<Transaction> filter(
            List<Transaction> transactions,
            String searchText,
            String category,
            LocalDate fromDate,
            LocalDate toDate) {

        List<Transaction> filtered =
                new ArrayList<>();

        for (Transaction transaction : transactions) {

            boolean matchesSearch =
                    searchText == null
                            || searchText.isBlank()
                            || transaction.getDescription()
                                    .toLowerCase()
                                    .contains(
                                            searchText
                                                    .toLowerCase()
                                    );

            boolean matchesCategory =
                    category == null
                            || category.equals("All")
                            || transaction.getCategory()
                                    .equalsIgnoreCase(category);

            boolean matchesFromDate =
                    fromDate == null
                            || !transaction.getDate()
                                    .isBefore(fromDate);

            boolean matchesToDate =
                    toDate == null
                            || !transaction.getDate()
                                    .isAfter(toDate);

            if (matchesSearch
                    && matchesCategory
                    && matchesFromDate
                    && matchesToDate) {

                filtered.add(transaction);
            }
        }

        return filtered;
    }
}