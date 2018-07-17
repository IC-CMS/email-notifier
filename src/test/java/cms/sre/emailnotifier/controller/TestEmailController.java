package cms.sre.emailnotifier.controller;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestEmailController {

    private JacksonTester<SendEmailRequest> jsonEmailRequest;
    private Logger logger = LoggerFactory.getLogger(TestEmailController.class);

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Before
    public void setup(){
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testAutowiring(){
        Assert.assertNotNull(testRestTemplate);
    }

    //Tests to see if Json requests can be parsed into Proper Object
    @Test
    public void jsonChecker() throws Exception{
        //Assume the Controller returns JSON:
        String jsonRequest = "{\"dn\":\"CN=Kiin Do Vah dvkiin1, OU=Whiterun, OU=Breezehome, OU=Empire, O=JarlBalgruuf, C=Tamriel\"," +
                "\"body\":\"There can be only ONE Body\"," +
                "\"subject\":\"I am the subject of King Email\"}";

        Assert.assertNotNull(this.jsonEmailRequest.parseObject(jsonRequest));

        Assert.assertNotEquals(this.jsonEmailRequest.parseObject(jsonRequest).getDn(), "");
        Assert.assertNotNull(this.jsonEmailRequest.parseObject(jsonRequest).getDn());
        Assert.assertEquals(this.jsonEmailRequest.parseObject(jsonRequest).getDn(), "CN=Kiin Do Vah dvkiin1, OU=Whiterun, OU=Breezehome, OU=Empire, O=JarlBalgruuf, C=Tamriel");

        Assert.assertNotEquals(this.jsonEmailRequest.parseObject(jsonRequest).getBody(), "");
        Assert.assertNotNull(this.jsonEmailRequest.parseObject(jsonRequest).getBody());
        Assert.assertEquals(this.jsonEmailRequest.parseObject(jsonRequest).getBody(), "There can be only ONE Body");

        Assert.assertNotEquals(this.jsonEmailRequest.parseObject(jsonRequest).getSubject(), "");
        Assert.assertNotNull(this.jsonEmailRequest.parseObject(jsonRequest).getSubject());
        Assert.assertEquals(this.jsonEmailRequest.parseObject(jsonRequest).getSubject(), "I am the subject of King Email");

    }


    @Test
    public void objectToJson() throws Exception{
        //Assume we have a JSON request to the Controller:
        String jsonRequest = "{\"dn\":\"CN=\",\"subject\":\"sub\",\"body\":\"Body\"}";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:" + port + "/sendEmail");
        StringEntity json = new StringEntity(jsonRequest);
        httpPost.setEntity(json);
        httpPost.setHeader("Accept","application/json");
        httpPost.setHeader("Content-Type","application/json");

        CloseableHttpResponse response = httpClient.execute(httpPost);

        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);

        logger.info(responseString);

        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        Assert.assertEquals(responseString, jsonRequest);
        Assert.assertNotNull(response);

    }





}
