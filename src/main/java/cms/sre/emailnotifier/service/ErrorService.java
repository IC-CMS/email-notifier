package cms.sre.emailnotifier.service;

import cms.sre.dna_common_data_model.emailnotifier.Email;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Configuration()
public class ErrorService implements ErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${emailnotifier.persistenceApiHost}")
    private String persistenceApiHost;

    @Value("${emailnotifier.persistenceApiPort}")
    private String persistenceApiPort;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageChannel smtpChannel;

    /**
     * Place a message that fails to send into the Persistent API Store
     * @param message
     */
    public void handleError(MimeMailMessage message) {

        try {
            logger.info("Failed to send message " + message.getMimeMessage().getSubject());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        persistMessageToDataStore(message);
    }

    @Override
    public void handleError(Throwable t) {
        stopEndpoints(t);
    }

    public void stopEndpoints(ErrorMessage errorMessage) {
        Throwable throwable = errorMessage.getPayload();
        stopEndpoints(throwable);
    }

    private void stopEndpoints(Throwable t) {
        //stoppingEndpoints
    }

    /**
     * The message is persisted to a MongoDB for later retry
     * @param mimeMailMessage
     * @return
     */
    private boolean persistMessageToDataStore(MimeMailMessage mimeMailMessage) {

        boolean success = false;

        Address[] addressesArray = null;
        String content = null;
        String subject = null;
        Date createdDate = null;

        try {

            addressesArray = mimeMailMessage.getMimeMessageHelper().getMimeMessage().getRecipients(Message.RecipientType.TO);
            content = (String) mimeMailMessage.getMimeMessage().getContent();
            subject = mimeMailMessage.getMimeMessage().getSubject();
            createdDate = mimeMailMessage.getMimeMessage().getSentDate();

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }

        String addresses = Arrays.asList(addressesArray)
                .stream()
                .map(a -> String.valueOf(a.toString()))
                .collect(Collectors.joining(","));

        JSONObject request = new JSONObject();

        request.put("emailAddress", addresses);
        request.put("subject", subject);
        request.put("body", content);
        request.put("createdDate", createdDate);
        request.put("uuid", UUID.randomUUID());

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(4), headers);

        try {

            String url = "http://" + persistenceApiHost + ":"
                    + this.persistenceApiPort + "/email";

            ResponseEntity<String> buildResponse = restTemplate.exchange(url,
                    HttpMethod.POST, entity, String.class);

            if (buildResponse.getStatusCode() == HttpStatus.OK) {

                logger.info("Successfully persisted email to data store");

                success = true;
            } else {

                logger.error("Failed to send email to data store");

                success = false;
            }

        } catch (Exception e) {

            logger.error("Connection Error: " + e.getMessage());

            success = false;

        }

        return success;
    }

    /**
     * Retrieve the list of Email messages from the persistent store
     * Another retrieve shouldn't start till this one is finished.
     */
    @Scheduled(fixedDelayString = "${emailnotifier.fixed.delay.prop}")
    public void retrieveMessagesFromDataStore() {

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

            List<Email> emails = response.getBody();

            if (emails != null) {

                for (Email email : emails) {

                    if (emailService.isValid(email)) {

                        // Delete the email from the persistent api repository
                        restTemplate.delete(url + "/" + email.getUuid());
                        // Put the email back on the queue to send
                        emailService.sendEmail(email);
                    }
                }
            }

        } catch (IllegalArgumentException e)  {

            logger.error("Illegal Arg: Error processing sendEmail request: " + e.getMessage());

        } catch (RestClientException e) {

            logger.error("Rest Client Error processing sendEmail request: " + e.getMessage());

        } catch (Exception e) {

            logger.error("Unknown Error processing sendEmail request: " + e.getCause());
        }
    }
}