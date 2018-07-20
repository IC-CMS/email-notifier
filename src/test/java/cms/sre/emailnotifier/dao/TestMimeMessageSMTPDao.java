package cms.sre.emailnotifier.dao;

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

import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMimeMessageSMTPDao {
    @Autowired
    SMTPDao smtpDao;

    private static Logger logger = LoggerFactory.getLogger(TestMimeMessageSMTPDao.class);

    /**
     * The sendsActualEmail test is disabled because it relies on sending an actual email, and given that the
     * host does not necessarily exist, the test would fail and the build would be unsuccessful.
     * The test, however, has been used with a fake SMTP server which has caught outbound traffic from this method.
      */
    @Ignore
    @Test
    public void sendsActualEmail(){
        Email email = new Email()
                .setSubject("Subject")
                .setBody("Body")
                .setEmailAddress("Dovahkiin@Dragonborn.com");
        MimeMessageSMTPDao mimeMessageSMTPDao = new MimeMessageSMTPDao("smtp.default.com", 25, "no-reply@default.com");
        Assert.assertTrue(mimeMessageSMTPDao.sendEmail(email));

    }
    @Test
    public void testAutowiring(){
        Assert.assertNotNull(smtpDao);
        Assert.assertTrue(smtpDao instanceof MimeMessageSMTPDao);
    }

}
