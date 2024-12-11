package com.bilvantis.controller;

import com.bilvantis.model.UserResponseDTO;
import com.bilvantis.service.EmailService;
import com.bilvantis.util.UserRequestResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }


    /**
     * Sends a start notification email for the specified process name.
     *
     * <p>This method is invoked through a POST request to the "/sendStartNotification" endpoint.
     * It triggers the {@code sendStartNotificationEmail} method of the {@code emailService}
     * to send an email notification and returns a response indicating the success of the operation.</p>
     *
     * @param processName the name of the process for which the start notification email is to be sent.
     *                    This value is provided in the request body.
     * @return a {@link ResponseEntity} containing a {@link UserResponseDTO} object
     *         that includes a success message indicating the email was sent.
     * @throws IllegalArgumentException if {@code processName} is null or empty.
     * @see EmailService#sendStartNotificationEmail(String)
     */

    @PostMapping("/sendStartNotification")
    public ResponseEntity<UserResponseDTO> sendStartNotification(@RequestParam String processName) {
        emailService.sendStartNotificationEmail(processName);
        return ResponseEntity.ok(UserRequestResponseBuilder.buildResponseDTO(null, null, "Start notification email sent successfully."));
    }
}
