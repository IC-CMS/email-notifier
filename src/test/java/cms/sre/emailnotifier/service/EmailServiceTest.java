
@RunWith(Springrunner.class)
@SpringBootTest(EmailServiceTest.class)
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
