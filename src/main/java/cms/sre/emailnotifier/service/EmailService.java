package cms.sre.emailnotifier.service;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.dao.SMTPDao;
import cms.sre.emailnotifier.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private String emailDomain;
    private SMTPDao smtpDao;

    @Autowired
    public EmailService(SMTPDao smtpDao, String emailDomain){
        this.smtpDao = smtpDao;
        this.emailDomain = emailDomain;
    }

    private static boolean isValid(SendEmailRequest emailRequest){
        if(!emailRequest.getDn().contains("CN=") || emailRequest.getDn().equals(null) || emailRequest.getDn().equals("") ){
            System.out.println("DN -->" + emailRequest.getDn());
            System.out.println("Error with DN Handling");
            return false;
        }
        else if(emailRequest.getBody().equals(null)){
            System.out.println("BODY -->" +"Error with Body Handling");
            return false;
            
        }
        else if(emailRequest.getSubject().equals(null)){
            System.out.println("Subject/Header -->" +"Error with Header Handling");
            return false;
        } 
        else{
            return true;
        }
    }

    private Email convert(SendEmailRequest emailRequest){
       String[] brokenDN = emailRequest.getDn().split(","); 
        String commonName = " ";
        for(int i =0; i < brokenDN.length; i++){
            if(brokenDN[i].contains("CN=")){
                commonName = brokenDN[i];
                i = brokenDN.length +1;
            }
        }
        String[] userDetails = commonName.split(" ");

        String email = userDetails[userDetails.length -1] + "@" + this.emailDomain;

        return new Email()
            .setSubject(emailRequest.getSubject())
            .setBody(emailRequest.getBody())
            .setEmailAddress(email);
    }

    public boolean sendEmail(SendEmailRequest emailRequest){
        boolean ret = false;

        if(isValid(emailRequest)){
            ret = this.smtpDao.sendEmail(convert(emailRequest));
        }

        return ret;
    }

}
