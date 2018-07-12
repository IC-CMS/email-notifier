package cms.sre.emailnotifier;

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

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }

    @Bean(name="emailDomain")
    public String externalEmailDomain(){
        return this.emailDomain;
    }
}
