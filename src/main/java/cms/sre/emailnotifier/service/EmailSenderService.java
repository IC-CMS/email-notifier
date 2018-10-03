package cms.sre.emailnotifier.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailSenderService {

    @Configuration
    @EnableIntegration
    public static class ContextConfiguration {

        File folder = new File("/tmp/email_notifier");

        @Value("${emailnotifier.smtpHost}")
        private String smtpHost;

        @Bean
        public MessageChannel smtpChannel() {
            return new DirectChannel();
        }

        @Bean
        public MessageChannel queueChannel() {

            return new QueueChannel();

        }

        @Bean(name = PollerMetadata.DEFAULT_POLLER)
        public PollerMetadata poller() {
            return Pollers.fixedRate(1000).get();
        }

        @Bean
        public MessageChannel customErrorChannel() {
            return MessageChannels.direct("customErrorChannel").get();
        }


        @Bean
        public IntegrationFlow customErrorFlow() {
            return IntegrationFlows.from(customErrorChannel())
                    .handle ("errorService", "handleError")
                    .get();
        }

        @Bean
        public IntegrationFlow sendMailFlow() {
            return IntegrationFlows.from("smtpChannel")
                    .channel(queueChannel())
                    .channel(customErrorChannel())
                    .enrichHeaders(Mail.headers())
                    .handle(Mail.outboundAdapter(smtpHost)
                                    .port(25)
                                    .protocol("smtp")
                                    .javaMailProperties(p -> p.put("mail.debug", "false")),
                            e -> e.id("sendMailEndpoint"))

                    .get();
        }
    }
}