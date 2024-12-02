package com.bilvantis.serviceImpl;

import com.bilvantis.exception.ApplicationException;
import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.util.AppModernizationProperties;
import com.bilvantis.util.EmailDetails;
import com.bilvantis.util.EmailService;
import com.bilvantis.util.EmailSupport;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

@Service("emailServiceImpl")
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    private final AppModernizationProperties appModernizationProperties;

    public EmailServiceImpl(JavaMailSender javaMailSender, AppModernizationProperties appModernizationProperties) {
        this.javaMailSender = javaMailSender;
        this.appModernizationProperties = appModernizationProperties;
    }

    /**
     * Sends an email for OTP generation using the provided email details and worker information.
     *
     * @param emailDetails EmailDetails
     * @param user         User
     */
    @Override
    public void sendMailOtpGeneration(EmailDetails emailDetails, UserInformationDTO user) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = EmailSupport.settingMimeMessageHelper(emailDetails, mimeMessage, appModernizationProperties.getSenderMailId());
            ClassPathResource emailTemplateResource = new ClassPathResource("otpEmail-Template.html");
            String emailTemplateContent = new String(FileCopyUtils.copyToByteArray(emailTemplateResource.getInputStream()), StandardCharsets.UTF_8);
            emailTemplateContent = emailTemplateContent.replace("${otp}", user.getOtp());
            emailTemplateContent = emailTemplateContent.replace("${name}", user.getFirstName());
            messageHelper.setText(emailTemplateContent, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error(String.format(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName()), e);
            throw new ApplicationException(e.getMessage());
        }
    }

}
