package com.threepmanagerapi.threepmanagerapi.mpesaintegration.controller;

import com.threepmanagerapi.threepmanagerapi.mpesaintegration.dto.InitiateTransactionDto;
import com.threepmanagerapi.threepmanagerapi.mpesaintegration.service.MpesaIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/transaction")
public class TransactionController {
    @Autowired
    private MpesaIntegrationService mpesaIntegrationService;
    @PostMapping("/create")
    public ResponseEntity createTransaction(@RequestBody InitiateTransactionDto initiateTransactionDto){
        return mpesaIntegrationService.initiateTransaction(initiateTransactionDto.getMsisdn(),initiateTransactionDto.getAmount(),initiateTransactionDto.getNarration(),initiateTransactionDto.getProductDispatchCode());
    }
}
