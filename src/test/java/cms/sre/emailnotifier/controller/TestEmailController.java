package cms.sre.emailnotifier.controller;

import cms.sre.dna_common_data_model.emailnotifier.Email;
import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.App;
import cms.sre.emailnotifier.service.EmailSenderService;
import cms.sre.emailnotifier.service.ErrorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestEmailController.TestAppConfig.class)
public class TestEmailController {

    private static JacksonTester<SendEmailRequest> jsonEmailRequest;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    private static Logger logger = LoggerFactory.getLogger(TestEmailController.class);

    @Autowired
    private EmailNotifierController controller;

    @Autowired
    private ErrorService errorService;

    @Autowired
    EmailSenderService emailSenderService;

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testAutowiring() {
        Assert.assertNotNull(testRestTemplate);
        Assert.assertNotNull(errorService);
        Assert.assertNotNull(emailSenderService);
    }

    @Test
    public void objectToJson() throws Exception {
        //ALWAYS Calls Email Service
        //Assume we have a JSON request to the Controller:
        String jsonRequest = "{\"dn\":\"CN=Doe John Smith jsdoe12\",\"subject\":\"sub\",\"body\":\"Body\"}";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:" + this.port + "/sendEmailWithDN");
        StringEntity json = new StringEntity(jsonRequest);
        httpPost.setEntity(json);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(httpPost);

        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);

        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        Assert.assertEquals(jsonRequest, responseString);
        Assert.assertNotNull(response);

    }

    @Test
    public void sendEmailObject() throws Exception {

        //ALWAYS Calls Email Service

        stubFor(post(urlEqualTo("/email"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", TEXT_PLAIN_VALUE)
                        .withBody("test")));

        RestTemplate rt = new RestTemplate();

        String uri = new String("http://localhost:" + port + "/sendEmail");

        Email email = new Email();
        email.setSubject("Test Email");
        email.setEmailAddress("dummy@dummy.com");
        email.setBody("Test Email");

        Email returns = rt.postForObject(uri, email, Email.class, "");

        Assert.assertNotNull(returns);

    }

    @Test
    public void sendEmailWithDnAddressTest() {

        SendEmailRequest request = new SendEmailRequest()
                .setSubject("sub")
                .setBody("bod")
                .setDn("CN=Doe John Smith jsdoe12");
        SendEmailRequest response = testRestTemplate.postForObject("http://localhost:" + this.port + "/sendEmailWithDN", request, SendEmailRequest.class);
        //rest template ALWAYS calls email service

        Assert.assertNotNull(response);

        Assert.assertEquals(request.getBody(), response.getBody());
        Assert.assertEquals(request.getSubject(), response.getSubject());
        Assert.assertEquals(request.getDn(), response.getDn());
    }

    @Test
    public void emailServiceCallsIncorrectly() {
        try {
            String jsonRequest = "{\"dn\":\"Doe John Smith jsdoe12\",\"subject\":\"sub\",\"body\":\"Body\"}";
            SendEmailRequest parsedEmailRequest = jsonEmailRequest.parseObject(jsonRequest);
            Assert.assertNotNull(parsedEmailRequest);

            SendEmailRequest response = controller.sendEmail(parsedEmailRequest);
            Assert.assertTrue(response.getSubject().equals("Bad Email Request"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SpringBootApplication
    public static class TestAppConfig extends App {


    }


}
