package com.threepmanagerapi.threepmanagerapi.mpesaintegration.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private String transactionID;
    private String partnerReferenceID;
    private String payerTransactionID;
    private String receiptNumber;
    private String narration;
    private BigDecimal amount;
    private TransactionStatus transactionStatus;
    private String productDispatchCode;
    private LocalDateTime transactionDate;
    private String msisdn;
    private boolean callbackReceived;
}
