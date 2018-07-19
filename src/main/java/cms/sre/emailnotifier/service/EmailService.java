package cms.sre.emailnotifier.service;

import cms.sre.dna_common_data_model.emailnotifier.SendEmailRequest;
import cms.sre.emailnotifier.dao.SMTPDao;
import cms.sre.emailnotifier.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;


@Service
public class EmailService {


    private String addressHost;
    private SMTPDao smtpDao;

    @Autowired
    public EmailService(SMTPDao smtpDao, @Qualifier("addressHost") String addressHost){
        this.smtpDao = smtpDao;
        this.addressHost = addressHost;

    }

    private static boolean isValid(SendEmailRequest emailRequest){

        boolean ret;
        if(!emailRequest.getDn().contains("CN=") || emailRequest.getDn().equals(null) || emailRequest.getDn().equals("") ){
            ret = false;

        }
        else if(emailRequest.getBody().equals(null) || emailRequest.getBody().equals("")){
            ret = false;


        }
        else if(emailRequest.getSubject().equals(null) || emailRequest.getSubject().equals("")){
            ret = false;

        }
        else{
            ret = true;

        }
        return ret;
    }

    private Email convert(SendEmailRequest emailRequest){
        System.out.println(addressHost);
        int idLength = 7;
        String email;
        String[] brokenDN = emailRequest.getDn().split(",");
        String commonName = " ";
        for(int i = 0, len = brokenDN.length; i < len; i++){
            if(brokenDN[i].contains("CN=")){
                commonName = brokenDN[i];
                i = brokenDN.length +1;
            }
        }
        String[] userDetails = commonName.split(" ");
        int length = userDetails.length;
        String id = userDetails[length - 1];
        //if the id at the end of the name String is 7 chars long...
        if(id.length() == idLength && !(id.equals(""))){
            email = id + "@" + addressHost;
        }
        else{
            email = "";
        }


        return new Email()
            .setSubject(emailRequest.getSubject())
            .setBody(emailRequest.getBody())
            .setEmailAddress(email);

    }

    public boolean sendEmail(SendEmailRequest emailRequest){
        boolean ret;

        if(isValid(emailRequest)){
            ret = this.smtpDao.sendEmail(convert(emailRequest));
        }
        else{
            ret = false;
        }
        return ret;
    }

}
