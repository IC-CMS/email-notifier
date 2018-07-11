package cms.sre.emailnotifier.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import cms.sre.emailnotifier.
@RunWith(Springrunner.class)
@SpringBootTest(classes = EmailServiceTest.class)
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
    Assert.assertTrue(emailService.sendEmail());
  }
  
  
}
