package cms.sre.emailnotifier.controller;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.dao.SMTPDao;
import cms.sre.emailnotifier.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailNotifierController {

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
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
