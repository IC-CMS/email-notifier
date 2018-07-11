
@RunWith(Springrunner.class)
@SpringBootTest(EmailServiceTest.class)
public class EmailServiceTest{
  //@Autowired
  //private EmailService emailService;
  
  @Mock
  SMTPDao mockSMTPDao;
  
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
