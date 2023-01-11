package headquarters.bank.exceptions;

/**
 * Exception class, which is thrown when the account does not exist in the list
 */
public class AccountDoesNotExistException extends Exception {
    /**
     * Constructor for the exception class, which is initialized with a given message
     *
     * @param message given message to be printed out
     */
    public AccountDoesNotExistException(final String message) {
        super(message);
    }
}
