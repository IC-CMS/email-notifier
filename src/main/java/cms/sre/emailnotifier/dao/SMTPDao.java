package cms.sre.emailnotifier.dao;

import cms.sre.emailnotifier.model.Email;

public interface SMTPDao {
    public boolean sendEmail(Email email);

}
