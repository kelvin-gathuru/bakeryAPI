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

}
