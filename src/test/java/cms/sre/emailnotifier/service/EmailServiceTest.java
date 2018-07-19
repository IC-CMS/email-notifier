package cms.sre.emailnotifier.service;

import cms.sre.emailnotifier.App;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.dao.SMTPDao;
import cms.sre.emailnotifier.model.Email;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class EmailServiceTest{

    @Autowired
    @Qualifier("addressHost")
    private String addressHost;

    private static Logger logger = LoggerFactory.getLogger(EmailServiceTest.class);

    private SMTPDao validatedEmailDao = new SMTPDao() {

        //checked After the request successfully converts to an Email
        @Override
        public boolean sendEmail(Email email) {
            final int nameLength = 7;
            String emailDomain = "@" + addressHost;
            System.out.println(emailDomain);
            System.out.println(email.getEmailAddress());

            Assert.assertNotNull(email);

            //Body Assertions
            Assert.assertNotNull(email.getBody());
            Assert.assertTrue(email.getBody().length() > 0);

            //Email Assertions
            Assert.assertNotNull(email.getEmailAddress());
            Assert.assertEquals(nameLength + emailDomain.length() , email.getEmailAddress().length());
            Assert.assertTrue(email.getEmailAddress().contains(emailDomain));

            //Subject Assertions
            Assert.assertNotNull(email.getSubject());
            Assert.assertTrue(email.getSubject().length() > 0);

            return true;
        }
    };

    private EmailService emailService = new EmailService(this.validatedEmailDao, "default.com");

    @Test
    public void sendsEmailProperly(){
        SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .setSubject("I am the subject of King Email")
                .setBody("There can be only ONE Body")
                .setDn("CN=Kiin Do Vah dvkiin1, OU=Whiterun, OU=Breezehome, OU=Empire, O=JarlBalgruuf, C=Tamriel");

        Assert.assertTrue(this.emailService.sendEmail(sendEmailRequest));
    }

    @Test
    public void noEmailWithIncompleteDn(){
        SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .setSubject("I am the subject")
                .setBody("Body")
                .setDn("");
        Assert.assertFalse(this.emailService.sendEmail(sendEmailRequest));
    }

}
