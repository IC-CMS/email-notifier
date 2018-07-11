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
    //Converts an Employee's Common Name String into a valid email request
    private static Email convert(SendEmailRequest emailRequest){
        String userDN = emailRequest.getDn();
        ArrayList<String> dnString = new ArrayList<String>(Arrays.asList(userDn.split(",")));
        
        for(int lcv = 0; lcv < dnString.size(); lcv++){
            if(dnString.get(lcv).contains("CN=")){
                userDN = dnString.get(lcv);
                lcv = dnString.size() + 1;
            }
        }
        String[] userDetails = userDN.split(" ");
        
        return new Email()
            .setHeader(emailRequest.getSubject())
            .setBody(emailRequest.getBody())
            .setRecipient(userDetails[userDetails.length - 1]);
    }

    public boolean sendEmail(SendEmailRequest emailRequest){
        boolean ret = false;

        if(isValid(emailRequest)){
            ret = this.smtpDao.sendEmail(convert(emailRequest));
        }

        return ret;
    }

}
