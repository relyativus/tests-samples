package co.inventorsoft.unit.unittestsdemo.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author anatolii vakaliuk
 */
@AllArgsConstructor
public class ValidationResult<T> {
    private Supplier<T> objectFactory;

    @Getter
    private Map<String, List<String>> errors;

    public boolean isValid() {
        return errors.isEmpty();
    }

    public T getObject() {
        if (!isValid()) {
            throw new ValidationException("Validation error");
        }
        return objectFactory.get();
    }
}
