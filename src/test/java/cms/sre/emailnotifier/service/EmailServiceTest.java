package cms.sre.emailnotifier.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
  //@Autowired
  //private EmailService emailService;
  
  @Mock
  SMTPDao mockSMTPDao;
  @Mock
  Email email;
  @Mock
  SendEmailRequest sendEmailRequest;
  
  @InjectMocks
  EmailService emailService;
  
  @Test
  public void testAutowiring(){
    Assert.assertNotNull(emailService);
  }
  
  @Test
  public void sendsEmailProperly(SendEmailRequest emailRequest){
    Assert.assertTrue(emailService.sendEmail(emailRequest));
  }
  
  
}
