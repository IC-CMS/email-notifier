package cms.sre.emailnotifier.service;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.dao.SMTPDao;
import cms.sre.emailnotifier.model.Email;
import cms.sre.emailnotifier.service.EmailService;
import cms.sre.emailnotifier.TestApp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApp.class)
public class EmailServiceTest{

  @Autowired
  private SendEmailRequest sendEmailRequest;
  @Mock
  SMTPDao mockSMTPDao;
  @Mock
  Email email;
  
  
  @InjectMocks
  EmailService emailService;
  
  @Test
  public void testAutowiring(){
    Assert.assertNotNull(sendEmailRequest);
    
  }
  
  @Test
  public void sendsEmailProperly(){
    sendEmailRequest
      .setSubject("I am the subject of King Email")
      .setBody("There can be only ONE Body")
      .setDn("CN=Kiin Do Va dvkiin1, OU=Whiterun, OU=Breezehome, OU=Empire, O=JarlBalgruuf, C=Tamriel");
    Assert.assertTrue(emailService.sendEmail(sendEmailRequest));
  }
  @Test()
  public void noEmailWithBlankDn(){
    sendEmailRequest
      .setSubject("I am the subject of King Email")
      .setBody("There can be only ONE Body")
      .setDn("");
      Assert.assertFalse(emailService.sendEmail(sendEmailRequest));
  }
  
  
}
