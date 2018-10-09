package cms.sre.emailnotifier.service;

import cms.sre.dna_common_data_model.emailnotifier.Email;
import cms.sre.emailnotifier.App;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class EmailServiceTest{

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Autowired
    @Qualifier("addressHost")
    private String addressHost;

    @Autowired
    EmailService emailService;

    private static Logger logger = LoggerFactory.getLogger(EmailServiceTest.class);

    @Test
    public void testAutowiring(){
        Assert.assertNotNull(emailService);
    }

    @Test
    public void sendsEmailProperly(){

        SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .setSubject("I am the subject of King Email")
                .setBody("There can be only ONE Body")
                .setDn("CN=Kiin Do Vah dvkiin1, OU=Whiterun, OU=Breezehome, OU=Empire, O=JarlBalgruuf, C=Tamriel");

        Assert.assertTrue(emailService.sendEmail(sendEmailRequest));
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
