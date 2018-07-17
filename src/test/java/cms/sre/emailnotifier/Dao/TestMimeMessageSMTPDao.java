package cms.sre.emailnotifier.Dao;

import cms.sre.emailnotifier.dao.MimeMessageSMTPDao;
import cms.sre.emailnotifier.model.Email;
import cms.sre.emailnotifier.service.EmailService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMimeMessageSMTPDao {

    private MimeMessageSMTPDao mimeMessageSMTPDao = new MimeMessageSMTPDao("smtp.default.com", "25");

    private Properties defaultProperties = new Properties();

    private static Logger logger = LoggerFactory.getLogger(TestMimeMessageSMTPDao.class);

    EmailService emailService;

    @Test
    public void checkAutowiring(){
        Assert.assertNotNull(mimeMessageSMTPDao);
        Assert.assertNotNull(defaultProperties);
    }

    @Test
    public void checkPort(){
        setDefaultProperties(mimeMessageSMTPDao.getHostname(),mimeMessageSMTPDao.getPort());
        Assert.assertEquals("25", defaultProperties.getProperty("mail.smtp.port"));
    }

    @Test
    public void verifyDomain(){
        setDefaultProperties(mimeMessageSMTPDao.getHostname(),mimeMessageSMTPDao.getPort());
        Assert.assertEquals("smtp.default.com", defaultProperties.getProperty("mail.smtp.host"));
    }
    //This Method bypasses the Service function and acts as if the email was verified as Valid.
    @Ignore
    @Test //TODO: Finish This Test!!
    public void sendsActualEmail(){
        //setDefaultProperties("", "");

    }
    public void setDefaultProperties(String hostname, String port){
        defaultProperties.put("mail.smtp.host", hostname);
        defaultProperties.put("mail.smtp.port", port);

    }

}
