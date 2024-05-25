package com.threepmanagerapi.threepmanagerapi.mpesaintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MpesaCallbackDto {
    private String statusCode;
    private String message;
    private String providerNarration;
    private String partnerTransactionID;
    private String payerTransactionID;
    private String receiptNumber;
    private String transactionID;
}
