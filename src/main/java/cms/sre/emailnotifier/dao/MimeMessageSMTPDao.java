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
import org.springframework.stereotype.Component;


@Component
public class MimeMessageSMTPDao implements SMTPDao{


    private String hostname;
    private String port;
    private Properties properties;

    @Autowired
    public MimeMessageSMTPDao(@Qualifier("emailDomain") String hostname, @Qualifier("smtpPort") String port){
        this.hostname = hostname;
        this.port = port;
        properties = new Properties();
    }

    @Override
    public boolean sendEmail(Email email) {
        this.properties.put("mail.smtp.host", hostname);
        this.properties.put("mail.smtp.port", port);
        Session session = Session.getDefaultInstance(this.properties);
        try{
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("CookieMonster@default.com"));
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

    public String getPort() {
        return port;
    }
    public MimeMessageSMTPDao setHostname(String hostname){
        this.hostname=hostname;
        return this;
    }
    public MimeMessageSMTPDao setPort(String port){
        this.port = port;
        return this;
    }
    public Properties getProperties(){
        return this.properties;
    }
}
