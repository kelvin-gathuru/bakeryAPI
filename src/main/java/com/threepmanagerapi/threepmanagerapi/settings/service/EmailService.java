package com.threepmanagerapi.threepmanagerapi.settings.service;

import com.threepmanagerapi.threepmanagerapi.settings.Repository.SettingsRepository;
import com.threepmanagerapi.threepmanagerapi.settings.Model.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
    @Value("${notification.api.url}")
    private String emailApiUrl;
    @Autowired
    private SettingsRepository settingsRepository;
    public void sendCredentials(String email, String password){
        String credentials = "::Email: " + email + "  ::Password: " + password;
        String payload = "{\"notificationCode\":\"PMANAGER-EMAIL\"," +
                "\"clientID\":1," +
                "\"message\":\"Hello Admin! These are your Bakery Manager Login Credentials! It is recommended that you change your password after logging into the system! " + credentials + "\"," +
                "\"subject\":\"Bakery Manager Credentials\"," +
                "\"recepient\":\"" + email + "\"," +
                "\"cCrecepients\":\"\"," +
                "\"bCCrecepients\":\"\"," +
                "\"type\":\"text/html\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                emailApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Email sent successfully");
        } else {
            log.info("Email not Sent");
        }

    }
    public void sendResetEmail(String email){
        List<Settings> settings = settingsRepository.findAll();
        log.info("Settings Size is {}",settings.size());

        Settings settings1 = settings.get(0);
        String url = settings1.getUrl();
        String resetLink = url+"reset-your-password?email=" + email;
        String payload = "{\"notificationCode\":\"PMANAGER-EMAIL\"," +
                "\"clientID\":1," +
                "\"message\":\"Click this link to reset your Bakery Manager password: " + resetLink + "\"," +
                "\"subject\":\"Reset Bakery Manager password\"," +
                "\"recepient\":\"" + email + "\"," +
                "\"cCrecepients\":\"\"," +
                "\"bCCrecepients\":\"\"," +
                "\"type\":\"text/html\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                emailApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Email sent successfully");
        } else {
            log.info("Email not Sent");
        }
    }

    public void sendProductDispatchCode(String email, String productDispatchCode, String date){
        String details = "::Dispatch Code: " + productDispatchCode + "  ::DateTime: " + date;
        String payload = "{\"notificationCode\":\"PMANAGER-EMAIL\"," +
                "\"clientID\":1," +
                "\"message\":\"Hello Agent! This is your Dispatch details! It is recommended that you don't loose this email before dispatch return! " + details + "\"," +
                "\"subject\":\"Bakery Manager Dispatch Code\"," +
                "\"recepient\":\"" + email + "\"," +
                "\"cCrecepients\":\"\"," +
                "\"bCCrecepients\":\"\"," +
                "\"type\":\"text/html\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                emailApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Email sent successfully");
        } else {
            log.info("Email not Sent");
        }

    }

    public void sendProductDispatchCodeSms(String phoneNumber, String productDispatchCode, String date){
        String details = "::Dispatch Code: " + productDispatchCode + "  ::DateTime: " + date;
        String payload = "{\"notificationCode\":\"PMANAGER-SMS\"," +
                "\"clientID\":1," +
                "\"message\":\"Hello Agent! This is your Dispatch details! It is recommended that you don't loose this SMS before dispatch return! " + details + "\"," +
                "\"subject\":\"Bakery Manager Dispatch Code\"," +
                "\"recepient\":\"" +0+phoneNumber+ "\"," +
                "\"cCrecepients\":\"\"," +
                "\"bCCrecepients\":\"\"," +
                "\"type\":\"text\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                emailApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("SMS sent successfully");
        } else {
            log.info("SMS not Sent");
        }

    }

    public void sendProductDispatchReturn(String email, String productDispatchCode, String date, String amount, String balance){
        String details = "::Amount Paid: KSh. " + amount + "  ::Balance: KSh. " + balance;
        String payload = "{\"notificationCode\":\"PMANAGER-EMAIL\"," +
                "\"clientID\":1," +
                "\"message\":\"Hello Agent! Your Dispatch of Code: "+ productDispatchCode+ "Has been returned on: " + date +". These are the details: " + details + "\"," +
                "\"subject\":\"Bakery Manager Dispatch Return\"," +
                "\"recepient\":\"" + email + "\"," +
                "\"cCrecepients\":\"\"," +
                "\"bCCrecepients\":\"\"," +
                "\"type\":\"text/html\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                emailApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Email sent successfully");

        } else {
            log.info("Email not Sent");
        }

    }
    public void sendProductDispatchReturnSms(String phone, String productDispatchCode, String date, String amount, String balance){
        String details = "::Amount Paid: KSh. " + amount + "  ::Balance: KSh. " + balance;
        String payload = "{\"notificationCode\":\"PMANAGER-SMS\"," +
                "\"clientID\":1," +
                "\"message\":\"Hello Agent! Your Dispatch of Code: "+ productDispatchCode+ "Has been returned on: " + date +". These are the details: " + details + "\"," +
                "\"subject\":\"Bakery Manager Dispatch Return\"," +
                "\"recepient\":\"" +0+phone+ "\"," +
                "\"cCrecepients\":\"\"," +
                "\"bCCrecepients\":\"\"," +
                "\"type\":\"text/html\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                emailApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("SMS sent successfully");
        } else {
            log.info("SMS not Sent");
        }

    }

}
