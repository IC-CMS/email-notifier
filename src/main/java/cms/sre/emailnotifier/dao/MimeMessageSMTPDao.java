package cms.sre.emailnotifier.dao;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import cms.sre.emailnotifier.model.Email;
import org.springframework.stereotype.Component;


@Component
public class MimeMessageSMTPDao implements SMTPDao{
    @Override
    public boolean sendEmail(Email email) {
        
       //Insert Portion Marking Engine Here
        
        Properties properties = System.getProperties();
        
        //insert properties relevant to us
        
        Session sess = Session.getDefaultInstance(properties);
        
        try{
            MimeMessage msg = new MimeMessage(sess);
            msg.setFrom(" ");//add reelevant address
            msg.addRecipients(Message.RecipientType.TO, email.getRecipient() + "")//domain name
                msg.setText(email.getBody());
            msg.setSubject(email.getHeader());
            Transport.send(msg);
            return true;
        }
        catch(Exception e){
        }
    }
}
