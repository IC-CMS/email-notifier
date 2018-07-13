package cms.sre.emailnotifier.controller;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TestEmailController {


    private MockMvc mockMvc;

    private JacksonTester<SendEmailRequest> jsonEmailRequest;

    private SendEmailRequest sendEmailRequest;

    private EmailNotifierController emailNotifierController;

    @Before
    public void setup(){
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(emailNotifierController).build();
    }
    @Test
    public void testToSeeifResponseisObject() throws Exception{
        sendEmailRequest
                .setSubject("I am the subject of King Email")
                .setBody("There can be only ONE Body")
                .setDn("CN=Kiin Do Vah dvkiin1, OU=Whiterun, OU=Breezehome, OU=Empire, O=JarlBalgruuf, C=Tamriel");

        RequestBuilder request = MockMvcRequestBuilders.post("/sendEmail",sendEmailRequest).accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();


        Assert.assertEquals(response.getStatus(),HttpStatus.OK.value());
        Assert.assertEquals(jsonEmailRequest.write(sendEmailRequest).getJson(), response.getContentAsString());


    }

}
