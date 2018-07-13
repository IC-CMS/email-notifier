package cms.sre.emailnotifier.dao;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cms.sre.emailnotifier.model.Email;
import org.springframework.stereotype.Component;


@Component
public class MimeMessageSMTPDao implements SMTPDao{

    private Properties properties = System.getProperties();
    private Session session = Session.getDefaultInstance(properties);

    @Override
    public boolean sendEmail(Email email) {

        //Insert  Marking Engine Here
        //insert properties relevant to us
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

    public Properties getDefaultProperties(){
        //TODO hydrate default?
        properties.put("mail.smtp.host","smtp.default.com");
        properties.put("mail.smtp.port", "25");
        return properties;
    }
    public Session getSession(){
        return session;
    }

}
