package cms.sre.emailnotifier.controller;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailNotifierController {

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public SendEmailRequest sendEmail(@RequestBody SendEmailRequest sendEmailRequest){
        emailService.sendEmail(sendEmailRequest);
        return sendEmailRequest;
    }
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public SendEmailRequest getEmail(){
        return new SendEmailRequest().setBody("body").setSubject("subject").setDn("Dn");
    }

}
