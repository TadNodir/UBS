package headquarters.bank;

import headquarters.bank.exceptions.TransactionAttributeException;

import java.util.Objects;

/**
 * Transfer is used in context of money transfers.
 *
 * @author tadno: Nodirjon Tadjiev
 * Date: 19.10.22
 */
public class Transfer extends Transaction {
    /**
     * Sender of the transfer
     */
    private String sender;
    /**
     * Recipient of the transfer
     */
    private String recipient;

    /**
     * The constructor with 5 initializing parameters
     *
     * @param date        represents the date of deposit, payout or any transaction (DD:MM:YYYY)
     * @param amount      value of deposit, payout or transaction
     * @param description extra caption of the transaction
     * @param sender      of the money
     * @param recipient   recipient of the money
     */
    public Transfer(String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException {
        super(date, amount, description);
        this.sender = sender;
        this.recipient = recipient;
    }

    /**
     * The copy constructor with 1 initializing parameters, which is an object
     *
     * @param transfer object, we are copying from
     */
    public Transfer(Transfer transfer) throws TransactionAttributeException {
        this(transfer.getDate(), transfer.getAmount(), transfer.getDescription(), transfer.sender, transfer.recipient);
    }

    /**
     * Overridden method, which contains the result of the parent class and returns accumulated result
     *
     * @param o object of the Object class
     * @return accumulated result of parent class equals and this class equals
     */
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(sender, transfer.sender) && Objects.equals(recipient, transfer.recipient);
    }

    /**
     * Method from the Transaction class in order to accept only positive input
     *
     * @param amount new amount
     */
    @Override
    public void setAmount(double amount) throws TransactionAttributeException {
        if (amount < 0) {
            super.setAmount(0);
            throw new TransactionAttributeException("Negative transfers are illegal.");
        } else {
            super.setAmount(amount);
        }
    }

    /**
     * Getter of transferred amount
     *
     * @return transferred amount
     */
    @Override
    public double getAmount() {
        return super.getAmount();
    }

    /**
     * Sender getter
     *
     * @return name of the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sender setter
     *
     * @param sender sets the name of the member
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Recipient getter
     *
     * @return recipient name
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Recipient setter
     *
     * @param recipient sets the name of the recipient
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }


    @Override
    public String toString() {
        return "Transfer: " + super.toString() +
                ", sender= " + sender +
                ", recipient= " + recipient;
    }

    public double calculate() {
        return getAmount();
    }
}
