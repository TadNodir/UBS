package headquarters.bank;

import headquarters.bank.exceptions.*;

import java.io.IOException;
import java.util.*;

/**
 * PrivateBankAlt represents implemented functions of the Bank interface, is different from PrivateBank only
 * by getAccountBalance method
 *
 * @author tadno: Nodirjon Tadjiev
 * Date: 11.11.22
 */
public class PrivateBankAlt implements Bank {
    /**
     * represents the name of private Bank
     */
    private String name;
    /**
     * represents the incomingInterest of private Bank
     */
    private double incomingInterest;
    /**
     * represents the outgoingInterest of private Bank
     */
    private double outgoingInterest;
    /**
     * represents the map of accounts and list of corresponding transactions of private Bank
     */
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    /**
     * Constructor for the Private bank to initialize an object with parameters
     *
     * @param name             of the bank
     * @param incomingInterest of the bank
     * @param outgoingInterest of the bank
     * @throws TransactionAttributeException is thrown, when incorrect incoming-/outgoing interest
     */
    public PrivateBankAlt(String name, double incomingInterest, double outgoingInterest) {
        this.name = name;
        this.incomingInterest = incomingInterest;
        this.outgoingInterest = outgoingInterest;
    }

    /**
     * Copy constructor, copies a private bank with the help of the object
     *
     * @param privateBankAlt bank parameter object
     * @throws TransactionAttributeException is thrown, when incorrect incoming-/outgoing interest
     */
    public PrivateBankAlt(PrivateBankAlt privateBankAlt) {
        this(privateBankAlt.name, privateBankAlt.incomingInterest, privateBankAlt.outgoingInterest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateBankAlt that = (PrivateBankAlt) o;
        return Double.compare(that.incomingInterest, incomingInterest) == 0 && Double.compare(that.outgoingInterest,
                outgoingInterest) == 0 && Objects.equals(name, that.name) && Objects.equals(accountsToTransactions,
                that.accountsToTransactions);
    }

    @Override
    public String toString() {
        return "PrivateBankAlt: " +
                "name=" + name +
                ", incomingInterest=" + incomingInterest +
                ", outgoingInterest=" + outgoingInterest;
    }

    /**
     * getter for the name of the Bank
     *
     * @return bank name
     */
    public String getName() {
        return name;
    }

    /**
     * setter for the bank name
     *
     * @param name of the bank to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for the incoming interest of the bank
     *
     * @param incomingInterest incoming interest if the bank to be set
     * @throws TransactionAttributeException is thrown, when the attributes are incorrect
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException {
        if (incomingInterest > 1 || incomingInterest < 0) {
            throw new TransactionAttributeException("Incorrect input of the outgoing interest. Must be between 0 and 1");
        }
        this.incomingInterest = incomingInterest;
    }

    /**
     * getter for the outgoing interest of the bank
     *
     * @return outgoing interest of the bank
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * Setter for the outgoing bank interest
     *
     * @param outgoingInterest is new outgoing interest value to be set
     * @throws TransactionAttributeException is thrown, when the attributes are incorrect
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException {
        if (outgoingInterest > 1 || outgoingInterest < 0) {
            throw new TransactionAttributeException("Incorrect input of the outgoing interest. Must be between 0 and 1");
        }
        this.outgoingInterest = outgoingInterest;
    }

    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Cannot create an account. Account already exists");
        }
        List<Transaction> transactions = new ArrayList<>();
        accountsToTransactions.put(account, transactions);
    }

    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException,
            TransactionAlreadyExistException, TransactionAttributeException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Cannot create an account. Account already exists");
        }
        Set<Transaction> set = new HashSet<>(transactions);
        if (set.size() < transactions.size()) {
            throw new TransactionAlreadyExistException("Given transactions list has duplicate transactions");
        }
        for (Transaction transaction : transactions) {
            if (transaction instanceof Transfer) {
                if (transaction.getAmount() < 0) {
                    throw new TransactionAttributeException("Negative transfers are illegal.");
                }
            } else if (transaction instanceof Payment) {
                if (((Payment) transaction).getIncomingInterest() > 1 || ((Payment) transaction).getIncomingInterest() < 0) {
                    throw new TransactionAttributeException("Incorrect input of the incoming interest. Must be between 0 and 1");
                } else if (((Payment) transaction).getOutgoingInterest() > 1 || ((Payment) transaction).getOutgoingInterest() < 0) {
                    throw new TransactionAttributeException("Incorrect input of the outgoing interest. Must be between 0 and 1");
                }
                ((Payment) transaction).setIncomingInterest(incomingInterest);
                ((Payment) transaction).setOutgoingInterest(outgoingInterest);
            }
        }
        accountsToTransactions.put(account, transactions);
    }

    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException,
            AccountDoesNotExistException, TransactionAttributeException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Cannot add transaction. Account does not exist");
        }
        for (Transaction t : accountsToTransactions.get(account)) {
            if (t.equals(transaction)) {
                throw new TransactionAlreadyExistException("This transaction already exists in this account");
            }
        }
        if (transaction instanceof Transfer) {
            if (transaction.getAmount() < 0) {
                throw new TransactionAttributeException("Negative transfers are illegal.");
            }
        } else if (transaction instanceof Payment) {
            if (((Payment) transaction).getIncomingInterest() > 1 || ((Payment) transaction).getIncomingInterest() < 0) {
                throw new TransactionAttributeException("Incorrect input of the incoming interest. Must be between 0 and 1");
            } else if (((Payment) transaction).getOutgoingInterest() > 1 || ((Payment) transaction).getOutgoingInterest() < 0) {
                throw new TransactionAttributeException("Incorrect input of the outgoing interest. Must be between 0 and 1");
            }
            ((Payment) transaction).setIncomingInterest(incomingInterest);
            ((Payment) transaction).setOutgoingInterest(outgoingInterest);
        }
        accountsToTransactions.get(account).add(transaction);
    }

    @Override
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Cannot remove transaction from non-existing account");
        }
        if (!accountsToTransactions.get(account).contains(transaction)) {
            throw new TransactionDoesNotExistException("Cannot remove transaction. Transaction does not exist in this account");
        }
        accountsToTransactions.get(account).remove(transaction);
    }

    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        // can also add AccountDoesNotExistException
        if (accountsToTransactions.containsKey(account)) {
            List<Transaction> trList = accountsToTransactions.get(account);
            return trList.contains(transaction);
        }
        return false;
    }

    @Override
    public double getAccountBalance(String account) {
        double accountBalance = 0;
        for (Transaction t : accountsToTransactions.get(account)) {
            if (t instanceof Transfer transfer) {
                accountBalance += transfer.calculate();
            } else if (t instanceof Payment payment) {
                accountBalance += payment.calculate();
            }
        }
        return accountBalance;
    }

    @Override
    public List<Transaction> getTransactions(String account) {
        return accountsToTransactions.get(account);
    }

    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        if (accountsToTransactions.containsKey(account)) {
            if (asc) {
                accountsToTransactions.get(account).sort(PrivateBankAlt.compareTransactionsAsc);
                return accountsToTransactions.get(account);
            }
            accountsToTransactions.get(account).sort(PrivateBankAlt.compareTransactionsDesc);
            return accountsToTransactions.get(account);
        }
        return null;
    }

    public static Comparator<Transaction> compareTransactionsAsc = (o1, o2) -> {
        double amount1 = o1.calculate();
        double amount2 = o2.calculate();

        return (int) (amount1 - amount2);
    };

    public static Comparator<Transaction> compareTransactionsDesc = (o1, o2) -> {
        double amount1 = o1.calculate();
        double amount2 = o2.calculate();

        return (int) (amount2 - amount1);
    };

    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> resultList = new ArrayList<>();
        if (accountsToTransactions.containsKey(account)) {
            if (positive) {
                for (Transaction t : accountsToTransactions.get(account)) {
                    if (t.calculate() >= 0) {
                        resultList.add(t);
                    }
                }
                return resultList;
            }
            for (Transaction t : accountsToTransactions.get(account)) {
                if (t.calculate() < 0) {
                    resultList.add(t);
                }
            }
            return resultList;
        }
        return null;
    }

    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Cannot delete an account, which does not exist");
        }
        accountsToTransactions.remove(account);
    }

    @Override
    public List<String> getAllAccounts() {
        return new ArrayList<>(accountsToTransactions.keySet());
    }
}
