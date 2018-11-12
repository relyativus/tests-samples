package co.inventorsoft.unit.unittestsdemo.email.model;

import co.inventorsoft.unit.unittestsdemo.core.TestValue;
import co.inventorsoft.unit.unittestsdemo.core.ValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author anatolii vakaliuk
 */
@RunWith(Parameterized.class)
public class CreateEmailMessageParameterizedTest {
    private static final String SUBJECT_ERROR_MSG = "Subject cannot be empty";

    @Parameter
    public TestValue<Map<String, Object>> testValue;

    @Parameters
    public static List<TestValue<Map<String, Object>>> invalidData() {
        return List.of(
                new TestValue<>(Map.of(), "subject", SUBJECT_ERROR_MSG),
                new TestValue<>(Map.of("subject", ""), "subject", SUBJECT_ERROR_MSG),
                new TestValue<>(Map.of("subject", " "), "subject", SUBJECT_ERROR_MSG)
        );
    }

    @Test
    public void validateShouldReturnValidationResultWithErrorsWhenDataIsInvalid() {
        final var createEmailMessageAttributes = new CreateEmailMessageAttributes(testValue.getValue());

        final ValidationResult<EmailMessage> validationResult = createEmailMessageAttributes.validate();
        assertFalse(validationResult.isValid());
        assertEquals(validationResult.getErrors().get(testValue.getField()), List.of(testValue.getErrorMessage()));
    }

}
