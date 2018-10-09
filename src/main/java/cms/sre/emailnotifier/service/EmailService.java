package cms.sre.emailnotifier.service;

import cms.sre.dna_common_data_model.emailnotifier.Email;
import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private MessageChannel smtpChannel;

    @Value("${emailnotifier.fromAddress}")
    private String fromAddress;

    @Value("${emailnotifier.addressHost}")
    private String addressHost;

    /**
     * Validatate that the email parsed correctly
     *
     * @param emailRequest
     * @return true or false
     */
    public boolean isValid(SendEmailRequest emailRequest) {

        logger.debug("Validating email: " + emailRequest.getSubject());

        boolean ret;
        if (emailRequest.getDn() == null || !emailRequest.getDn().contains("CN=") || emailRequest.getDn().equals("")) {
            ret = false;

        } else if (emailRequest.getBody() == null || emailRequest.getBody().equals("")) {
            ret = false;


        } else if (emailRequest.getSubject() == null || emailRequest.getSubject().equals("")) {
            ret = false;

        } else {
            ret = true;

        }
        return ret;
    }

    /**
     * Validate that the email parsed correctly
     *
     * @param email
     * @return true or false
     */
    public boolean isValid(Email email) {

        logger.debug("Validating email: " + email.getSubject());

        boolean ret;
        if (email.getEmailAddress() == null || !email.getEmailAddress().contains("@") || email.getEmailAddress().equals("")) {
            ret = false;

        } else if (email.getBody() == null || email.getBody().equals("")) {
            ret = false;


        } else if (email.getSubject() == null || email.getSubject().equals("")) {
            ret = false;

        } else {
            ret = true;

        }
        return ret;
    }

    /**
     * Send an email as constructed
     *
     * @param email
     * @return true or false
     */
    public boolean sendEmail(Email email) {

        logger.debug("Calling sendEmail");

        boolean result = false;

        JavaMailSender mailSender = new JavaMailSenderImpl();

        MimeMessage rootMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(rootMessage, false);
            helper.setFrom(fromAddress);
            helper.setSubject(email.getSubject());
            helper.setText(email.getBody());
            helper.setTo(email.getEmailAddress());

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        MailMessage message = new MimeMailMessage(helper);

        result = smtpChannel.send(MessageBuilder.withPayload(message).build());

        return result;
    }

    /**
     * Accepts an email with a DN and converts to email address
     * @param sendEmailRequest
     * @return true or false
     */
    public boolean sendEmail(SendEmailRequest sendEmailRequest) {

        boolean result = false;

        if(isValid(sendEmailRequest)){
            Email email = convert(sendEmailRequest);
            result = this.sendEmail(email);
        }
        else{
            result = false;
        }

        return result;
    }

    /**
     * Convert an DN that comes in to a email address
     * Note: used a fixed hostname for who the email goes to
     *
     * @param emailRequest
     * @return a converted email
     */
    private Email convert(SendEmailRequest emailRequest) {
        int idLength = 7;
        String email;
        String[] brokenDN = emailRequest.getDn().split(",");
        String commonName = " ";
        for (int i = 0, len = brokenDN.length; i < len; i++) {
            if (brokenDN[i].contains("CN=")) {
                commonName = brokenDN[i];
                i = brokenDN.length + 1;
            }
        }
        String[] userDetails = commonName.split(" ");
        int length = userDetails.length;
        String id = userDetails[length - 1];
        //if the id at the end of the name String is 7 chars long...
        if (id.length() == idLength && !(id.equals(""))) {
            email = id + "@" + addressHost;
        } else {
            email = "";
        }

        Email newEmail = new Email();
        newEmail.setUuid(UUID.randomUUID().toString());
        newEmail.setSubject(emailRequest.getSubject());
        newEmail.setBody(emailRequest.getBody());
        newEmail.setEmailAddress(email).setUuid(UUID.randomUUID().toString());

        return newEmail;
    }

    @Configuration
    @EnableIntegration
    public static class ContextConfiguration {

        @Bean
        public MessageChannel smtpChannel() {
            return new DirectChannel();
        }

    }
}