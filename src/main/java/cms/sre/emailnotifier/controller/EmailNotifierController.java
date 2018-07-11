package cms.sre.emailnotifier.controller;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailNotifierController {

    @Autowired
    private EmailService emailService;

    @RequestMapping("/sendEmail")
    public SendEmailRequest sendEmail(SendEmailRequest sendEmailRequest){
        
        emailService.sendEmail(sendEmailRequest);
        return sendEmailRequest;
    }

}
