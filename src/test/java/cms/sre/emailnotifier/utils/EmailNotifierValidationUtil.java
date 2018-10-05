package cms.sre.emailnotifier.utils;

import cms.sre.dna_common_data_model.emailnotifier.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class EmailNotifierValidationUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotifierValidationUtil.class);

    public static void main(String[] args) {

        logger.info("Validating email notifier responds");

        try {

            RestTemplate rt = new RestTemplate();

            String uri = new String("http://localhost:" + "9091" + "/sendEmail");

            Email email = new Email();
            email.setSubject("Test Email");
            email.setEmailAddress("dummy@dummy.com");
            email.setBody("Test Email");

            Email returns = rt.postForObject(uri, email, Email.class);

            if (returns == null) {
                logger.info("Not working as expected");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
