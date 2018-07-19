package cms.sre.emailnotifier.dao;

import cms.sre.emailnotifier.model.Email;
import cms.sre.emailnotifier.service.EmailService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMimeMessageSMTPDao {

    private MimeMessageSMTPDao mimeMessageSMTPDao = new MimeMessageSMTPDao("smtp.default.com", 25);

    private Properties defaultProperties = new Properties();

    private static Logger logger = LoggerFactory.getLogger(TestMimeMessageSMTPDao.class);

    EmailService emailService;

    //This Method bypasses the Service function and acts as if the email was verified as Valid.
    @Ignore
    @Test
    public void sendsActualEmail(){
        Email email = new Email()
                .setSubject("Subject")
                .setBody("Body")
                .setEmailAddress("Dovahkiin@Dragonborn.com");
        MimeMessageSMTPDao mimeMessageSMTPDao = new MimeMessageSMTPDao("smtp.default.com", 25);
        Assert.assertTrue(mimeMessageSMTPDao.sendEmail(email));

    }
    public void setDefaultProperties(String hostname, String port){
        defaultProperties.put("mail.smtp.host", hostname);
        defaultProperties.put("mail.smtp.port", port);

    }

}
