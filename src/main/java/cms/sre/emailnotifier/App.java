package cms.sre.emailnotifier;

import cms.sre.emailnotifier.dao.MimeMessageSMTPDao;
import cms.sre.emailnotifier.dao.SMTPDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {


    @Value("${emailnotifier.smtpHost:localhost}")
    private String emailHost;

    @Value("${emailnotifier.smtpPort:25}")
    private int emailPort;

    @Value("${emailnotifier.addressHost:default.com}")
    private String addressHost;

    @Value("${emailnotifier.defaultSender:no-reply@default.com}")
    private String defaultSender;

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }

    @Bean(name = "emailHost")
    public String getEmailHost(){
        return emailHost;
    }

    @Bean
    public SMTPDao smtpDao(String emailHost){
        return new MimeMessageSMTPDao(emailHost, this.emailPort, this.defaultSender);
    }

    @Bean(name = "addressHost")
    public String getAddressHost(){ return this.addressHost; }

    @Bean(name = "defaultSender")
    public String getDefaultSender(){return  this.defaultSender;}

}
