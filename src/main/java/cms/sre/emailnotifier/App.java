package cms.sre.emailnotifier;

import cms.sre.emailnotifier.dao.MimeMessageSMTPDao;
import cms.sre.emailnotifier.dao.SMTPDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
@SpringBootApplication
public class App {

    //@Value("${emailnotifier.emailDomain:default}")
    //private String emailDomain;

    @Value("${emailnotifier.smtpHost:localhost}")
    private String emailHost;

    @Value("${emailnotifier.smtpPort:25}")
    private int emailPort;

    @Value("${emailnotifier.addressHost:default.com}")
    private String addressHost;

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }

    @Bean(name = "emailHost")
    public String getEmailHost(){
        return emailHost;
    }

    @Bean
    public SMTPDao smtpDao(String emailHost){
        return new MimeMessageSMTPDao(emailHost, this.emailPort);
    }

    @Bean(name = "addressHost")
    public String getAddressHost(){ return this.addressHost; }

}
