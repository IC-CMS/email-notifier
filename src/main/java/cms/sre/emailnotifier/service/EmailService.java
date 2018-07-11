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
        if(!emailRequest.getDn().contains("CN=") || emailRequest.getDn().equals(null) || emailRequest.getDn.equals("") ){
            return false;
        }
        else if(emailRequest.getBody().equals(null)){
            return false;
        }
        else if(emailRequest.getHeader().equals(null)){
            return false;
        } 
        else{
            return true;
        }
    }

    private static Email convert(SendEmailRequest emailRequest){
       String[] brokenDN = emailrequest.getDn().split(","); 
        String commonName;
        for(int i =0; i < brokenDN; i++){
            if(brokenDN[i].contains("CN=")){
                commonName = brokenDN[i];
                i = brokenDn.length +1;
            }
        }
        String[] userDetails = commonName.split(" ");
        return new Email()
            .setHeader(emailRequest.getSubject())
            .setBody(emailRequest.getBody())
            .setRecipient(userDetails[userDetails.length -1]);
    }

    public boolean sendEmail(SendEmailRequest emailRequest){
        boolean ret = false;

        if(isValid(emailRequest)){
            ret = this.smtpDao.sendEmail(convert(emailRequest));
        }

        return ret;
    }

}
