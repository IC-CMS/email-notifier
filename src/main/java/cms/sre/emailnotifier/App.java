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

    @Value("${emailnotifier.emailDomain:default}")
    private String emailDomain;

    @Value("${emailnotifier.smtpHost:default")
    private String emailHost;

    @Value("${emailnotifier.smtpPort:25")
    private int emailPort;

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }

    @Bean(name="emailDomain")
    public String externalEmailDomain(){
        return this.emailDomain;
    }

    @Bean
    public SMTPDao smtpDao(){
        return new MimeMessageSMTPDao(this.emailHost, this.emailPort);
    }
}
