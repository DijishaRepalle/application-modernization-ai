package com.bilvantis.serviceImpl;

import com.bilvantis.exception.ApplicationException;
import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.service.EmailService;
import com.bilvantis.util.AppModernizationProperties;
import com.bilvantis.util.EmailDetails;
import com.bilvantis.util.EmailSupport;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

import static com.bilvantis.util.EmailServiceImplConstants.*;

@Service("emailServiceImpl")
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    private final AppModernizationProperties appModernizationProperties;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, AppModernizationProperties appModernizationProperties) {
        this.javaMailSender = javaMailSender;
        this.appModernizationProperties = appModernizationProperties;
    }

    /**
     * Sends an email for OTP generation using the provided email details and worker information.
     *
     * @param emailDetails EmailDetails
     * @param user  User
     */
    @Override
    public void sendMailOtpGeneration(EmailDetails emailDetails, UserInformationDTO user) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = EmailSupport.settingMimeMessageHelper(emailDetails, mimeMessage, appModernizationProperties.getSenderMailId());
            ClassPathResource emailTemplateResource = new ClassPathResource(OTP_EMAIl_TEMPLATE);
            String emailTemplateContent = new String(FileCopyUtils.copyToByteArray(emailTemplateResource.getInputStream()), StandardCharsets.UTF_8);
            emailTemplateContent = emailTemplateContent.replace(OTP, user.getOtp());
            emailTemplateContent = emailTemplateContent.replace(NAME, user.getFirstName());
            messageHelper.setText(emailTemplateContent, TRUE);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error(String.format(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName()), e);
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * Sends a notification email to a distribution list indicating the start of a process.
     * <p>
     * This method constructs and sends an email using a predefined template. The email
     * contains details about the process name and its initiation status. The recipients,
     * sender email address, and template are configured via application properties.
     *
     * @param processName the name of the process that has been initiated, included in the email.
     * @throws ApplicationException if an error occurs during email composition or sending.
     */
    @Override
    public void sendStartNotificationEmail(String processName) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            String distributionList = appModernizationProperties.getDistributionList();
            String[] recipients = distributionList.split(",\\s*");
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(appModernizationProperties.getSenderMailId());
            messageHelper.setTo(recipients);

            ClassPathResource emailTemplateResource = new ClassPathResource(PROCESS_START_NOTIFICATION_TEMPLATE);
            String emailTemplateContent = new String(FileCopyUtils.copyToByteArray(emailTemplateResource.getInputStream()), StandardCharsets.UTF_8);

            emailTemplateContent = emailTemplateContent.replace(PROCESS_NAME, processName);
            emailTemplateContent = emailTemplateContent.replace(STATUS, INITIATED);

            messageHelper.setSubject(PROCESS_START_NOTIFICATION);
            messageHelper.setText(emailTemplateContent, TRUE);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error(String.format(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName()), e);
            throw new ApplicationException(e.getMessage());
        }
    }

}