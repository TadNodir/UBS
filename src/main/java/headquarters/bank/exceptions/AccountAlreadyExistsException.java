package headquarters.bank.exceptions;

/**
 * Exception class, which is thrown when the account already exists
 */
public class AccountAlreadyExistsException extends Exception {
    /**
     * Constructor for the exception class, which is initialized with a given message
     *
     * @param message given message to be printed out
     */
    public AccountAlreadyExistsException(final String message) {
        super(message);
    }
}
