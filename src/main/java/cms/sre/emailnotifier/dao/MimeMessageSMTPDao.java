package cms.sre.emailnotifier.dao;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cms.sre.emailnotifier.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

public class MimeMessageSMTPDao implements SMTPDao{


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
        try{
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(emailAddress));
            msg.addRecipients(Message.RecipientType.TO, email.getEmailAddress());
            msg.setText(email.getBody());
            msg.setSubject(email.getSubject());
            Transport.send(msg);
            return true;
        }
        catch(Exception e){
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
