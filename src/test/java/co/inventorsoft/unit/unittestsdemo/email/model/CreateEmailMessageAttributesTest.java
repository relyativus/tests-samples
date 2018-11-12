package co.inventorsoft.unit.unittestsdemo.email.model;

import co.inventorsoft.unit.unittestsdemo.core.TestValue;
import co.inventorsoft.unit.unittestsdemo.core.ValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author anatolii vakaliuk
 */
public class CreateEmailMessageAttributesTest {

    @Test
    public void validateShouldCreateNewEmailMessageObjectWhenAllValidationRulesAreSatisfied() {
        final var expectedMailMessage = new EmailMessage(null, "test@gmail.com", "Hello", "Email body",
                Instant.now().plus(1, ChronoUnit.MINUTES));
        final var createEmailMessageValidation = new CreateEmailMessageAttributes(Map.of(
                "subject", expectedMailMessage.getSubject(),
                "receiver", expectedMailMessage.getReceiver(),
                "body", expectedMailMessage.getBody(),
                "deliverAt", expectedMailMessage.getDeliverAt()
        ));
        final ValidationResult<EmailMessage> validationResult = createEmailMessageValidation.validate();

        assertTrue(validationResult.isValid());
        assertEquals(expectedMailMessage, validationResult.getObject());
    }

}