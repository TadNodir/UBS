package headquarters.bank;

import headquarters.bank.exceptions.TransactionAttributeException;

import java.util.Objects;

/**
 * Abstract class, which is used by all transactions
 *
 * @author tadno: Nodirjon Tadjiev
 * Date: 19.10.22
 */
public abstract class Transaction implements CalculateBill {
    /**
     * represents date of the transaction
     */
    protected String date;
    /**
     * represents amount of the transaction
     */
    protected double amount;
    /**
     * represents description of the transaction
     */
    protected String description;

    /**
     * @param date        represents the date of deposit, withdrawal or any transaction (DD:MM:YYYY)
     * @param description extra caption of the transaction
     */
    public Transaction(String date, double amount, String description) throws TransactionAttributeException {
        this.date = date;
        setAmount(amount);
        this.description = description;
    }

    /**
     * Overridden method from Object class
     *
     * @return date, amount and description of the transaction as String
     */
    @Override
    public String toString() {
        return "Date= " + date + ", amount= " + calculate() +
                ", description= " + description;
    }

    /**
     * Overridden method from Object class
     *
     * @param o object of the Object class, which helps with comparing attribures
     * @return true, if all attributes are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(date, that.date) && Objects.equals(description, that.description);
    }

    /**
     * Getter for returning the transaction date
     *
     * @return transaction date
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter to set a transaction date
     *
     * @param date is a new transaction date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Transaction amount getter
     *
     * @return amount of transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Transaction amount setter
     *
     * @param amount new amount value
     */
    public void setAmount(double amount) throws TransactionAttributeException {
        this.amount = amount;
    }

    /**
     * Getter for returning the transaction description
     *
     * @return transaction description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter to set a transaction description
     *
     * @param description is a new transaction description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

