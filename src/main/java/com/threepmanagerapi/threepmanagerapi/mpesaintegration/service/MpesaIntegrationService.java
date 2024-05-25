package com.threepmanagerapi.threepmanagerapi.mpesaintegration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threepmanagerapi.threepmanagerapi.mpesaintegration.model.Transaction;
import com.threepmanagerapi.threepmanagerapi.mpesaintegration.model.TransactionStatus;
import com.threepmanagerapi.threepmanagerapi.mpesaintegration.repository.TransactionRepository;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
public class MpesaIntegrationService {
    @Value("${paymentCode}")
    private String paymentCode;
    @Value("${paymentOption}")
    private String paymentOption;
    @Value("${serviceCode}")
    private String serviceCode;
    @Value("${accountNumber}")
    private String accountNumber;
    @Value("${partnerCallbackUrl}")
    private String partnerCallbackUrl;
    @Value("${mpesa.authenticate.url}")
    private String mpesaAuthUrl;
    @Value("${mpesa.initiate.url}")
    private String mpesaInitiateUrl;
    @Value("${mpesa.username}")
    private String username;
    @Value("${mpesa.password}")
    private String password;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ResponseService responseService;

    public String authenticateTransaction() {
        String payload = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Create the request entity
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                mpesaAuthUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            String token = responseBody.split("\"token\":\"")[1].split("\"")[0];
            return token;
        } else {
            log.info(response.toString());
            return response.toString();
        }

    }
    public ResponseEntity initiateTransaction(String msisdn, BigDecimal amount, String narration, String productDispatchCode){
        String token = authenticateTransaction();
        String partnerReferenceID = String.valueOf(UUID.randomUUID());
        String payload = "{"
                + "\"paymentCode\":\"" + paymentCode + "\","
                + "\"paymentOption\":\"" + paymentOption + "\","
                + "\"serviceCode\":\"" + serviceCode + "\","
                + "\"msisdn\":\"" + msisdn + "\","
                + "\"accountNumber\":\"" + accountNumber + "\","
                + "\"partnerCallbackUrl\":\"" + partnerCallbackUrl + "\","
                + "\"amount\":" + amount + ","
                + "\"partnerReferenceID\":\"" + partnerReferenceID + "\","
                + "\"narration\":\"" + narration + "\""
                + "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        // Create the request entity
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                mpesaInitiateUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            String transactionID = responseBody.split("\"transactionID\":\"")[1].split("\"")[0];
            String partnerReference = responseBody.split("\"partnerReferenceID\":\"")[1].split("\"")[0];
            log.info(response.getBody());
            Transaction transaction = new Transaction();
            transaction.setTransactionID(transactionID);
            transaction.setPartnerReferenceID(partnerReference);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setAmount(amount);
            transaction.setMsisdn(msisdn);
            transaction.setNarration(narration);
            transaction.setTransactionStatus(TransactionStatus.UNSUCCESSFUL);
            transaction.setProductDispatchCode(productDispatchCode);
            transaction.setCallbackReceived(false);
            transactionRepository.save(transaction);
            return responseService.formulateResponse(
                    transaction,
                    "Payment initiated successfully",
                    HttpStatus.OK,
                    null,
                    true
            );
        }

        return responseService.formulateResponse(
                null,
                "Payment not initiated ",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                false
        );
    }
}
