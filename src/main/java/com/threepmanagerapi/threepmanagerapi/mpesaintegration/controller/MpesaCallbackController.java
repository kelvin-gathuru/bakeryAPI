package com.threepmanagerapi.threepmanagerapi.mpesaintegration.controller;

import com.threepmanagerapi.threepmanagerapi.mpesaintegration.dto.MpesaCallbackDto;
import com.threepmanagerapi.threepmanagerapi.mpesaintegration.model.Transaction;
import com.threepmanagerapi.threepmanagerapi.mpesaintegration.model.TransactionStatus;
import com.threepmanagerapi.threepmanagerapi.mpesaintegration.repository.TransactionRepository;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("api/mpesa")
public class MpesaCallbackController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ResponseService responseService;
    @PostMapping("/callback")
    public ResponseEntity handleCallback(@RequestBody MpesaCallbackDto callbackData) {
        System.out.println(callbackData.toString());
        String statusCode = callbackData.getStatusCode();
        String transactionID = callbackData.getTransactionID();
        Transaction transaction = transactionRepository.findByTransactionID(transactionID);
        transaction.setCallbackReceived(true);
        transactionRepository.save(transaction);
        if(Objects.equals(statusCode, "00")){
            String payerTransactionID = callbackData.getPayerTransactionID();
            String receiptNumber = callbackData.getReceiptNumber();
            transaction.setPayerTransactionID(payerTransactionID);
            transaction.setReceiptNumber(receiptNumber);
            transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
            transactionRepository.save(transaction);
            return responseService.formulateResponse(
                    callbackData,
                    "Payment done successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        }
        return responseService.formulateResponse(
                null,
                "Payment unsuccessful ",
                HttpStatus.UNAUTHORIZED,
                null,
                false
        );
    }
    @GetMapping("/status")
    public ResponseEntity getTransactionStatus(@RequestParam ("transactionID") String transactionID){
        Transaction transaction = transactionRepository.findByTransactionID(transactionID);
        if(!transaction.isCallbackReceived()){
            return responseService.formulateResponse(
                    null,
                    "Callback not received",
                    HttpStatus.OK,
                    null,
                    false
            );
        }
        return responseService.formulateResponse(
                transaction,
                "Callback received successfully",
                HttpStatus.OK,
                null,
                true
        );
    }

}
