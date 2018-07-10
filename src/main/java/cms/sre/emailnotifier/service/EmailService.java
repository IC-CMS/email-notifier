package cms.sre.emailnotifier.service;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.dao.SMTPDao;
import cms.sre.emailnotifier.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private SMTPDao smtpDao;

    @Autowired
    public EmailService(SMTPDao smtpDao){
        this.smtpDao = smtpDao;
    }

    private static boolean isValid(SendEmailRequest emailRequest){
        return true;
    }

    private static Email convert(SendEmailRequest emailRequest){
        return new Email();
    }

    public boolean sendEmail(SendEmailRequest emailRequest){
        boolean ret = false;

        if(isValid(emailRequest)){
            ret = this.smtpDao.sendEmail(convert(emailRequest));
        }

        return ret;
    }

}
