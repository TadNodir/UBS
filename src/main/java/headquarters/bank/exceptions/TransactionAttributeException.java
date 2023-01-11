package headquarters.bank.exceptions;

/**
 * Exception class, which is thrown when incorrect attribute input is detected
 */
public class TransactionAttributeException extends Exception {
    /**
     * Constructor for the exception class, which is initialized with a given message
     *
     * @param message given message to be printed out
     */
    public TransactionAttributeException(final String message) {
        super(message);
    }
}
