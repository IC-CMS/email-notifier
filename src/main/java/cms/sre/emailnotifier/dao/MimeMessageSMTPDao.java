package cms.sre.emailnotifier.dao;

import cms.sre.emailnotifier.model.Email;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class MimeMessageSMTPDao implements SMTPDao{

    private String hostname;
    private int port;

    public MimeMessageSMTPDao(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

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
        Properties properties = new Properties();
        properties.put("mail.smtp.host",this.hostname);
        properties.put("mail.smtp.port", this.port);
        return properties;
    }
    public Session getSession(){
        return session;
    }

}
