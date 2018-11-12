package co.inventorsoft.unit.unittestsdemo.core;

import lombok.Value;

/**
 * @author anatolii vakaliuk
 */
@Value
public class TestValue<T> {
    private T value;
    private String field;
    private String errorMessage;
}
