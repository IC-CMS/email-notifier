package cms.sre.emailnotifier.Dao;

import cms.sre.emailnotifier.dao.MimeMessageSMTPDao;
import org.junit.Assert;
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

    private MimeMessageSMTPDao mimeMessageSMTPDao = new MimeMessageSMTPDao();

    private Properties defaultProperties = mimeMessageSMTPDao.getDefaultProperties();
    private static Logger logger = LoggerFactory.getLogger(TestMimeMessageSMTPDao.class);


    @Test
    public void checkAutowiring(){
        Assert.assertNotNull(mimeMessageSMTPDao);
        Assert.assertNotNull(defaultProperties);
    }

    @Test
    public void checkPort(){

        Assert.assertEquals("25", defaultProperties.getProperty("mail.smtp.port"));
    }

    @Test
    public void verifyDomain(){

        //TODO hydrate domain name?

        Assert.assertEquals("smtp.default.com", defaultProperties.getProperty("mail.smtp.host"));

    }
}
