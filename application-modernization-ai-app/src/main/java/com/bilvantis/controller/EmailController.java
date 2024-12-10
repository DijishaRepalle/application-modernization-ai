package com.bilvantis.controller;

import com.bilvantis.model.UserResponseDTO;
import com.bilvantis.service.EmailService;
import com.bilvantis.util.UserRequestResponseBuilder;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserResponseDTO> sendStartNotification(@RequestBody String processName) {
        emailService.sendStartNotificationEmail(processName);
        return ResponseEntity.ok(UserRequestResponseBuilder.buildResponseDTO(null, null, "Start notification email sent successfully."));
    }
}
