package cms.sre.emailnotifier.controller;

import cms.sre.dna_common_data_model.emailnotifier.Email;
import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailNotifierController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public Email sendEmail(@RequestBody Email email){

        logger.info("Received email request from " + email.getEmailAddress());
        logger.info("Received email request from " + email.getSubject());

        boolean sent = emailService.sendEmail(email);

        if(sent){
            //do something
            return email;
        }
        else{
            //do something better than this
            return email.setSubject("Bad Email Request");

        }


    }

    @RequestMapping(value = "/sendEmailWithDN", method = RequestMethod.POST)
    public SendEmailRequest sendEmail(@RequestBody SendEmailRequest sendEmailRequest){
        boolean sent = emailService.sendEmail(sendEmailRequest);
        if(sent){
            //do something
            return sendEmailRequest;
        }
        else{
            //do something better than this
            return sendEmailRequest.setSubject("Bad Email Request");

        }


    }

}
