package cms.sre.emailnotifier.dao;

import cms.sre.emailnotifier.model.Email;
import org.springframework.stereotype.Component;

@Component
public class MimeMessageSMTPDao implements SMTPDao{
    @Override
    public boolean sendEmail(Email email) {
        return true;
    }
}
