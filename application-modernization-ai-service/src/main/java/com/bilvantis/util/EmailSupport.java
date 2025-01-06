package com.bilvantis.util;

import com.bilvantis.exception.ApplicationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.nio.charset.StandardCharsets;

public class EmailSupport {
    /**
     * Creates and configures an EmailDetails object with the provided recipient mail ID and subject.
     *
     * @param mailId  String
     * @param subject String
     * @return EmailDetails
     */
    public static EmailDetails settingEmailDetails(String mailId, String subject) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(mailId);
        emailDetails.setSubject(subject);
        return emailDetails;

    }

    /**
     * Creates and configures a MimeMessageHelper for sending an email with the provided email details.
     *
     * @param emailDetails EmailDetails
     * @param mimeMessage  MimeMessage
     * @param senderMailId String
     * @return MimeMessageHelper
     */
    public static MimeMessageHelper settingMimeMessageHelper(EmailDetails emailDetails, MimeMessage mimeMessage, String senderMailId) throws MessagingException {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
            messageHelper.setFrom(senderMailId);
            messageHelper.setTo(emailDetails.getRecipient());
            messageHelper.setSubject(emailDetails.getSubject());
            return messageHelper;
        } catch (MessagingException e) {
            throw new ApplicationException(e);
        }

    }
}
