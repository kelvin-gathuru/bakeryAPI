package com.threepmanagerapi.threepmanagerapi.settings.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendCredentials(String email, String password) {
        String subject = "Bakery Manager Credentials";
        String message = String.format(
            "Hello Admin!\n\n" +
            "These are your Bakery Manager Login Credentials!\n" +
            "It is recommended that you change your password after logging into the system.\n\n" +
            "Email: %s\n" +
            "Password: %s\n\n" +
            "Best regards,\nBakery Manager Team",
            email, password
        );
        
        sendEmail(email, subject, message);
    }
    
    public void sendResetEmail(String email) {
        String resetLink = "http://www.3modernsystems.co.ke/bakery-ui/reset-your-password?email=" + email;
        String subject = "Reset Bakery Manager password";
        String message = String.format(
            "Hello,\n\n" +
            "Click this link to reset your Bakery Manager password:\n\n" +
            "%s\n\n" +
            "Best regards,\nBakery Manager Team",
            resetLink
        );
        
        sendEmail(email, subject, message);
    }

    public void sendProductDispatchCode(String email, String productDispatchCode, String date) {
        String subject = "Bakery Manager Dispatch Code";
        String message = String.format(
            "Hello Agent!\n\n" +
            "This is your Dispatch details!\n" +
            "It is recommended that you don't lose this email before dispatch return!\n\n" +
            "Dispatch Code: %s\n" +
            "Date: %s\n\n" +
            "Best regards,\nBakery Manager Team",
            productDispatchCode, date
        );
        
        sendEmail(email, subject, message);
    }

    public void sendProductDispatchReturn(String email, String productDispatchCode, String date, String amount, String balance) {
        String subject = "Bakery Manager Dispatch Return";
        String message = String.format(
            "Hello Agent!\n\n" +
            "Your Dispatch of Code: %s has been returned on: %s.\n\n" +
            "Amount Paid: KSh. %s\n" +
            "Balance: KSh. %s\n\n" +
            "Best regards,\nBakery Manager Team",
            productDispatchCode, date, amount, balance
        );
        
        sendEmail(email, subject, message);
    }

    public void sendProductDispatchCodeSms(String phone, String dispatchCode, String date) {
        log.info("SMS notification - Dispatch Code: {}, Phone: {}, Date: {}", dispatchCode, phone, date);
    }

    public void sendProductDispatchReturnSms(String phone, String productDispatchCode, String date, String amount, String balance) {
        log.info("SMS notification - Dispatch Return - Code: {}, Phone: {}, Date: {}, Amount: {}, Balance: {}", 
                productDispatchCode, phone, date, amount, balance);
    }
    
    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
