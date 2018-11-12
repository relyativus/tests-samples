package co.inventorsoft.unit.unittestsdemo.storage;

import co.inventorsoft.unit.unittestsdemo.email.model.EmailMessage;
import co.inventorsoft.unit.unittestsdemo.error.ApplicationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author anatolii vakaliuk
 */
public class EmailStorageTest {

    private JdbcOperations jdbcOperations;
    private PreparedStatement preparedStatement;
    private EmailStorage emailStorage;

    @Before
    public void init() {
        this.jdbcOperations = Mockito.mock(JdbcOperations.class);
        this.preparedStatement = Mockito.mock(PreparedStatement.class);
        this.emailStorage = new EmailStorage(jdbcOperations);
        when(jdbcOperations.execute(anyString(), any(PreparedStatementCallback.class))).thenAnswer(invocationOnMock -> {
            PreparedStatementCallback statementCallback = invocationOnMock.getArgument(1);
            return statementCallback.doInPreparedStatement(preparedStatement);
        });
    }

    @Test
    public void createShouldExecuteInsertQueryAndReturnEmailMessageWhenQueryExecutedSuccessfully() throws Exception {
        final var sampleEmailMessage = new EmailMessage(null, "test@gmail.com", "Hello Insert",
                "Email body", Instant.ofEpochSecond(1542062745));
        when(preparedStatement.executeUpdate()).thenReturn(1);

        emailStorage.create(sampleEmailMessage);

        verify(preparedStatement, times(1)).setString(1, sampleEmailMessage.getReceiver());
        verify(preparedStatement, times(1)).setString(2, sampleEmailMessage.getSubject());
        verify(preparedStatement, times(1)).setString(3, sampleEmailMessage.getBody());
        verify(preparedStatement, times(1))
                .setTimestamp(4, Timestamp.from(sampleEmailMessage.getDeliverAt()));
        verify(jdbcOperations, times(1))
                .execute(eq("INSERT INTO pending_emails (receiver, subject, body, deliver_at) VALUES(?,?,?,?)"),
                        any(PreparedStatementCallback.class));
    }

    @Test(expected = ApplicationException.class)
    public void createShouldThrowApplicationExceptionWhenExecuteUpdateReturnsZeroAffectedCount() throws Exception {
        final var sampleEmailMessage = new EmailMessage(1L, "test@gmail.com", "Hello Insert",
                "Email body", Instant.now());
        when(preparedStatement.executeUpdate()).thenReturn(0);

        emailStorage.create(sampleEmailMessage);
    }
}