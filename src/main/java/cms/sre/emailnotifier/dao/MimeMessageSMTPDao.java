package cms.sre.emailnotifier.dao;

import cms.sre.emailnotifier.model.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MimeMessageSMTPDao implements SMTPDao{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String emailAddress;
    private String hostname;
    private int port;

    public MimeMessageSMTPDao(String hostname, int port, String emailAddress){
        this.hostname = hostname;
        this.port = port;
        this.emailAddress = emailAddress;

    }

    @Override
    public boolean sendEmail(Email email) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", hostname);
        properties.put("mail.smtp.port", String.valueOf(port));
        Session session = Session.getDefaultInstance(properties);

        MimeMessage msg = new MimeMessage(session);

        try{

            msg.setFrom(new InternetAddress(emailAddress));
            msg.addRecipients(Message.RecipientType.TO, email.getEmailAddress());
            msg.setText(email.getBody());
            msg.setSubject(email.getSubject());
            Transport.send(msg);

            logger.info("Successfully sent email");
            logger.info("Email message sent to: " + email.getEmailAddress() + " via smtp host " + hostname);
            logger.info("Email message subject: " + email.getSubject());
            logger.debug("Email body: " + email.getBody());

            return true;
        }
        catch(Exception e){

            logger.info("Failed to send email: " + e.getMessage());
            logger.info("Email message sent to: " + email.getEmailAddress() + " via smtp host " + hostname);
            logger.info("Email message subject: " + email.getSubject());
            logger.debug("Email body: " + email.getBody());


            return false;
        }
    }

    public int getPort() {
        return port;
    }

    public MimeMessageSMTPDao setHostname(String hostname){
        this.hostname=hostname;
        return this;
    }
    public MimeMessageSMTPDao setPort(int port){
        this.port = port;
        return this;
    }

}
