package cms.sre.emailnotifier.health;

import cms.sre.dna_common_data_model.emailnotifier.Email;
import cms.sre.emailnotifier.controller.EmailNotifierController;
import cms.sre.emailnotifier.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@Component
@ComponentScan("cms.sre")
public class HealthCheck implements HealthIndicator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${emailnotifier.persistenceApiHost}")
    private String persistenceApiHost;

    @Value("${emailnotifier.persistenceApiPort}")
    private String persistenceApiPort;

    @Autowired
    private EmailNotifierController emailNotifierController;

    @Autowired
    private EmailSenderService emailSenderService;

    public HealthCheck() {
    }

    @Override
    public Health health() {

        int errorCode = checkEmailNotifierController();

        if (errorCode != 0) {
            return Health.down()
                    .withDetail("EmailNotifierController Error", errorCode).build();
        }

        errorCode = this.checkEmailSenderService();

        if (errorCode != 0) {
            return Health.down()
                    .withDetail("EmailSenderService Error", errorCode).build();
        }

        errorCode = this.checkConnectionToPersistenceApi();

        if (errorCode != 0) {
            return Health.down()
                    .withDetail("EmailSenderService Error, Persistence API not responding", errorCode).build();
        }
        return Health.up().build();
    }


    private int checkEmailNotifierController() {

        if (emailNotifierController == null) {
            return 1;
        }

        return 0;
    }

    private int checkEmailSenderService() {

        if (emailSenderService == null) {
            return 1;
        }

        return 0;
    }

    private int checkConnectionToPersistenceApi() {

        int status = 0;

        final String url = "http://" + persistenceApiHost + ":"
                + persistenceApiPort + "/email";

        try {

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<Email>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Email>>() {
                    });

            if (response.getStatusCodeValue() == 200) {

                status = 0;

            } else {
                status = 1;
            }

        } catch (Exception e) {

            status = 1;
            logger.error("Unable to connect to persistence-api host: " + e.getMessage());

        }

        return status;
    }
}