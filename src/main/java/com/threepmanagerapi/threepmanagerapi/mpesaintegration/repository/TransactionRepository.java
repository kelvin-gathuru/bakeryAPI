package com.threepmanagerapi.threepmanagerapi.mpesaintegration.repository;

import com.threepmanagerapi.threepmanagerapi.mpesaintegration.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Transaction findByTransactionID(String transactionID);
}
