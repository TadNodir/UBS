package headquarters.bank.exceptions;

/**
 * Exception class, which is thrown when the transaction already in the list
 */
public class TransactionAlreadyExistException extends Exception {
    /**
     * Constructor for the exception class, which is initialized with a given message
     *
     * @param message given message to be printed out
     */
    public TransactionAlreadyExistException(final String message) {
        super(message);
    }
}
