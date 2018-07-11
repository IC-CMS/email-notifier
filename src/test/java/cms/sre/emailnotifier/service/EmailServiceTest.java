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

  
  @Mock
  SMTPDao mockSMTPDao;
  @Mock
  Email email;
  
  
  @InjectMocks
  EmailService emailService;
 
  @Test
  public void sendsEmailProperly(){
    SendEmailRequest sendEmailRequest = new SendEmailRequest()
      sendEmailRequest.setSubject("I am the subject of King Email");
      sendEmailRequest.setBody("There can be only ONE Body");
      sendEmailRequest.setDn("CN=Kiin Do Va dvkiin1, OU=Whiterun, OU=Breezehome, OU=Empire, O=JarlBalgruuf, C=Tamriel");
    Assert.assertTrue(emailService.sendEmail(sendEmailRequest));
  }
  @Test()
  public void noEmailWithBlankDn(){
   SendEmailRequest sendEmailRequest = new SendEmailRequest()
      sendEmailRequest.setSubject("I am the subject of King Email");
      sendEmailRequest.setBody("There can be only ONE Body");
      sendEmailRequest.setDn("");
      Assert.assertFalse(emailService.sendEmail(sendEmailRequest));
  }
  
  
}
