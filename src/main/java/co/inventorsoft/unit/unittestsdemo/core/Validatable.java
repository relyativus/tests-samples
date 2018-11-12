package co.inventorsoft.unit.unittestsdemo.core;

/**
 * @author anatolii vakaliuk
 */
public interface Validatable<T> {
    ValidationResult<T> validate();
}
