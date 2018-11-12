package co.inventorsoft.unit.unittestsdemo.email.model;

import co.inventorsoft.unit.unittestsdemo.core.Validatable;
import co.inventorsoft.unit.unittestsdemo.core.ValidationConstraints;
import co.inventorsoft.unit.unittestsdemo.core.ValidationResult;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author anatolii vakaliuk
 */
@AllArgsConstructor
public class CreateEmailMessageAttributes implements Validatable<EmailMessage> {

    private final Map<String, Object> requestBody;

    @Override
    public ValidationResult<EmailMessage> validate() {
        Map<String, List<String>> errorsContainer = new HashMap<>();
        final String receiver = Optional.ofNullable(requestBody.get("receiver")).map(Object::toString).orElse(null);
        final String subject = Optional.ofNullable(requestBody.get("subject")).map(Object::toString).orElse(null);
        final String body = Optional.ofNullable(requestBody.get("body")).map(Object::toString).orElse(null);
        final Instant deliverAt = Optional.ofNullable(requestBody.get("deliverAt")).map(value -> value instanceof Double
                ? Instant.ofEpochSecond(((Double) value).intValue())
                : Instant.class.cast(value))
                .orElse(null);

        ValidationConstraints.checkBlank(subject, "subject", "Subject cannot be empty", errorsContainer);
        ValidationConstraints.checkBlank(receiver, "receiver", "Receiver cannot be empty", errorsContainer);
        ValidationConstraints.checkBlank(body, "body", "Body cannot be empty", errorsContainer);
        ValidationConstraints.checkDateInFuture(deliverAt, "deliverAt", "Deliver at should be future date",
                errorsContainer);

        return new ValidationResult<>(
                () -> new EmailMessage(null, receiver, subject, body, deliverAt),
                errorsContainer
        );
    }
}
