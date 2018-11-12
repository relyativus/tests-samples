package co.inventorsoft.unit.unittestsdemo.error;

/**
 * @author anatolii vakaliuk
 */
public class ApplicationException extends RuntimeException {
    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message) {
        super(message);
    }
}
