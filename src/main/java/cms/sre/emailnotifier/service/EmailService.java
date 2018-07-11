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
        if(!emailRequest.getDn().contains("CN=") || emailRequest.getDn().equals(null) || emailRequest.getDn().equals("") ){
            
            System.out.println("Error with DN Handling");
            return false;
        }
        else if(emailRequest.getBody().equals(null)){
            return false;
            System.out.println("Error with Body Handling");
        }
        else if(emailRequest.getSubject().equals(null)){
            System.out.println("Error with Header Handling");
            return false;
        } 
        else{
            return true;
        }
    }

    private static Email convert(SendEmailRequest emailRequest){
       String[] brokenDN = emailRequest.getDn().split(","); 
        String commonName = " ";
        for(int i =0; i < brokenDN.length; i++){
            if(brokenDN[i].contains("CN=")){
                commonName = brokenDN[i];
                i = brokenDN.length +1;
            }
        }
        String[] userDetails = commonName.split(" ");
        return new Email()
            .setSubject(emailRequest.getSubject())
            .setBody(emailRequest.getBody())
            .setEmailAddress(userDetails[userDetails.length -1]);
    }

    public boolean sendEmail(SendEmailRequest emailRequest){
        boolean ret = false;

        if(isValid(emailRequest)){
            ret = this.smtpDao.sendEmail(convert(emailRequest));
        }

        return ret;
    }

}
