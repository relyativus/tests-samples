package co.inventorsoft.unit.unittestsdemo.storage;

import co.inventorsoft.unit.unittestsdemo.email.model.EmailMessage;
import co.inventorsoft.unit.unittestsdemo.error.ApplicationException;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

/**
 * @author anatolii vakaliuk
 */
@AllArgsConstructor
@Component
public class EmailStorage {

    private JdbcOperations jdbcOperations;

    public EmailMessage create(final EmailMessage emailMessage) {
        final Integer affectedCount = jdbcOperations.execute(
                "INSERT INTO pending_emails (receiver, subject, body, deliver_at) VALUES(?,?,?,?)",
                (PreparedStatement smt) -> {
                    smt.setString(1, emailMessage.getReceiver());
                    smt.setString(2, emailMessage.getSubject());
                    smt.setString(3, emailMessage.getBody());
                    smt.setTimestamp(4, Timestamp.from(emailMessage.getDeliverAt()));
                    return smt.executeUpdate();
                });
        if (affectedCount != 1) {
            throw new ApplicationException("Email message insert failure");
        }
        return emailMessage;
    }
}
