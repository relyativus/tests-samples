package co.inventorsoft.unit.unittestsdemo.core;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author anatolii vakaliuk
 */
@UtilityClass
public class ValidationConstraints {

    public static void checkBlank(final String value, final String field, final String message, final Map<String, List<String>> errors) {
        final boolean isValid = Objects.nonNull(value) && value.trim().length() > 0;
        addError(field, message, errors, isValid);
    }

    public static void checkDateInFuture(Instant value, String field, String message, Map<String, List<String>> errorsContainer) {
        final boolean isValid = Objects.requireNonNullElse(value, Instant.MIN).isAfter(Instant.now());
        addError(field, message, errorsContainer, isValid);
    }

    private static void addError(String field, String message, Map<String, List<String>> errors, boolean isValid) {
        if (!isValid) {
            errors.compute(field, (key, errorsContainer) -> {
                List<String> initializedContainer = Objects.requireNonNullElse(errorsContainer, new ArrayList<>());
                initializedContainer.add(message);
                return initializedContainer;
            });
        }
    }
}
