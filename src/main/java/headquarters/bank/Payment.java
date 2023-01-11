package headquarters.bank;

import headquarters.bank.exceptions.TransactionAttributeException;

/**
 * Payment represents  deposit, withdrawal or any transaction of the bank
 *
 * @author tadno: Nodirjon Tadjiev
 * Date: 19.10.22
 */
public class Payment extends Transaction {
    private double incomingInterest;
    private double outgoingInterest;

    /**
     * The constructor with 5 initializing parameters
     *
     * @param date             represents the date of deposit, payout or any transaction (DD:MM:YYYY)
     * @param amount           value of deposit, payout or transaction
     * @param description      extra caption of the transaction
     * @param incomingInterest Interest charges of the deposit (has to be between 0 and 1)
     * @param outgoingInterest Interest charges of the withdrawal (has to be between 0 and 1)
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outgoingInterest)
            throws TransactionAttributeException {
        super(date, amount, description);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     * The copy constructor with 1 initializing parameters, which is an object
     *
     * @param payment object, we are copying from
     */
    public Payment(Payment payment) throws TransactionAttributeException {
        this(payment.getDate(), payment.getAmount(), payment.getDescription(), payment.incomingInterest,
                payment.outgoingInterest);
    }

    /**
     * Incoming interest getter
     *
     * @return incoming interest
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * Incoming interest setter
     *
     * @param incomingInterest parameter to set
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException {
        if (incomingInterest > 1 || incomingInterest < 0) {
            throw new TransactionAttributeException("Incorrect input of the incoming interest. Must be between 0 and 1");
        } else {
            this.incomingInterest = incomingInterest;
        }
    }

    /**
     * Outgoing interest getter
     *
     * @return outgoing interest
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * Outgoing interest setter
     *
     * @param outgoingInterest parameter to set
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException {
        if (outgoingInterest > 1 || outgoingInterest < 0) {
            throw new TransactionAttributeException("Incorrect input of the outgoing interest. Must be between 0 and 1");
        } else {
            this.outgoingInterest = outgoingInterest;
        }
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
        Payment payment = (Payment) o;
        return Double.compare(payment.incomingInterest, incomingInterest) == 0 &&
                Double.compare(payment.outgoingInterest, outgoingInterest) == 0;
    }

    /**
     * Prints out all the attribute values of the class
     *
     * @return output of the transfer data, containing attributes of upper class and this class
     */
    @Override
    public String toString() {
        return "Payment: " + super.toString() +
                ", incoming interest= " + getIncomingInterest() +
                ", outgoing interest= " + getOutgoingInterest();
    }

    @Override
    public double calculate() {
        if (getAmount() > 0) {
            return (1 - getIncomingInterest()) * getAmount();
        } else {
            return (1 + getOutgoingInterest()) * getAmount();
        }
    }

    /**
     * Setter of an amount of transfer
     *
     * @param amount new set amount
     */
    @Override
    public void setAmount(double amount) throws TransactionAttributeException {
        super.setAmount(amount);
    }

    /**
     * Overridden method of the amount, which returns calculated payment amount with account of interest tax
     *
     * @return payment amount with account of interest tax
     */
    @Override
    public double getAmount() {
        return super.getAmount();
    }
}
