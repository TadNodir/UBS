package headquarters.bank.exceptions;

/**
 * Exception class, which is thrown when there is no transaction found in the list
 */
public class TransactionDoesNotExistException extends Exception {
    /**
     * Constructor for the exception class, which is initialized with a given message
     *
     * @param message given message to be printed out
     */
    public TransactionDoesNotExistException(final String message) {
        super(message);
    }
}
