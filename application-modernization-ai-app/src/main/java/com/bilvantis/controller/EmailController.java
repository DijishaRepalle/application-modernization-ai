package com.bilvantis.controller;

import com.bilvantis.service.EmailService;
import com.bilvantis.util.EmailDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }


    @PostMapping("/sendStartNotification")
    public String sendStartNotification(@RequestBody String processName) {
        try {
            emailService.sendStartNotificationEmail(processName);
            return "Start notification email sent successfully.";
        } catch (Exception e) {
            return "Error sending start notification email: " + e.getMessage();
        }
    }
}
