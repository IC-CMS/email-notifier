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


@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class MimeMessageSMTPDao implements SMTPDao{

    //update relevant to us, the default value currently set is not a representaiton of an actual email
    @Value("${emailnotifier.defaultSender:FAKE_EMAIL@default.com}")
    private String emailAddress;

    private String hostname;
    private int port;

    public MimeMessageSMTPDao(String hostname, int port){
        this.hostname = hostname;
        this.port = port;

    }

    @Override
    public boolean sendEmail(Email email) {
        System.out.println(emailAddress);
        System.out.println(hostname);
        System.out.println(port);
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

    public String getHostname(){
        return hostname;
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
