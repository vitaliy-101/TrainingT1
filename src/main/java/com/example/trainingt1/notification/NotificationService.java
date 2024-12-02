package com.example.trainingt1.notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;

    public void sendSimpleEmail(String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo("dimakk2@gmail.com");
            helper.setSubject("Test");
            helper.setText(body);
            mailSender.send(message);
            log.info("Email sent successfully!");
        } catch (MailException | MessagingException e) {
            log.error(String.format("Error while sending email messages: '%s'", e.getMessage()));
            throw new RuntimeException(e.getMessage());
        }
    }
}