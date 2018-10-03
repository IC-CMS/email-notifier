package cms.sre.emailnotifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
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

    @Bean(name = "addressHost")
    public String getAddressHost(){ return this.addressHost; }

    @Bean(name = "defaultSender")
    public String getDefaultSender(){return  this.defaultSender;}

}
