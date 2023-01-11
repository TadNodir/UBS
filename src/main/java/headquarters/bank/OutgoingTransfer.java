package headquarters.bank;

import headquarters.bank.exceptions.TransactionAttributeException;

/**
 * Class represents outgoing transfers from the account
 */
public class OutgoingTransfer extends Transfer {
    /**
     * Constructor of the outgoing transfer class
     *
     * @param date        of the transfer
     * @param amount      of money in transfer
     * @param description of the transfer
     * @param sender      of the transfer
     * @param recipient   of the transfer
     * @throws TransactionAttributeException is thrown, when incorrect attributes are input
     */
    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException {
        super(date, amount, description, sender, recipient);
    }

    /**
     * As it is an outgoing transfer representation, money amount is subtracted
     *
     * @return negative amount, because outgoing transfer
     */
    @Override
    public double calculate() {
        return (-1) * (super.calculate());
    }
}
