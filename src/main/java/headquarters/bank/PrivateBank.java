package headquarters.bank;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import headquarters.bank.exceptions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * PrivateBank represents implemented functions of the Bank interface
 *
 * @author tadno: Nodirjon Tadjiev
 * Date: 11.11.22
 */
public class PrivateBank implements Bank {
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
    public String directoryName = "src/main/java/headquarters/bank/transactions/";

    /**
     * Constructor for the Private bank to initialize an object with parameters
     *
     * @param name             of the bank
     * @param incomingInterest of the bank
     * @param outgoingInterest of the bank
     * @throws TransactionAttributeException is thrown, when incorrect incoming-/outgoing interest
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest) throws
            TransactionAttributeException, IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException {
        this.name = name;
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
        readAccounts();
    }

    /**
     * Copy constructor, copies a private bank with the help of the object
     *
     * @param privateBank bank parameter object
     * @throws TransactionAttributeException is thrown, when incorrect incoming-/outgoing interest
     */
    public PrivateBank(PrivateBank privateBank) throws TransactionAttributeException, IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException {
        this(privateBank.name, privateBank.incomingInterest, privateBank.outgoingInterest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateBank that = (PrivateBank) o;
        return Double.compare(that.incomingInterest, incomingInterest) == 0 && Double.compare(that.outgoingInterest,
                outgoingInterest) == 0 && Objects.equals(name, that.name) && Objects.equals(accountsToTransactions,
                that.accountsToTransactions);
    }

    @Override
    public String toString() {
        return "PrivateBank: " +
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
     * getter fo the incoming interest of the bank
     *
     * @return incoming interest
     */
    public double getIncomingInterest() {
        return incomingInterest;
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
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Cannot create an account. Account already exists");
        }
        List<Transaction> transactions = new ArrayList<>();
        accountsToTransactions.put(account, transactions);
        writeAccount(account);
    }

    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException,
            TransactionAlreadyExistException, TransactionAttributeException, IOException, AccountDoesNotExistException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Cannot create an account. Account already exists");
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
            }
        }
        createAccount(account);
        for (Transaction t : transactions) {
            addTransaction(account, t);
        }
        // accountsToTransactions.put(account, transactions);
        writeAccount(account);
    }

    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException,
            AccountDoesNotExistException, TransactionAttributeException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Cannot add transaction. Account does not exist");
        }
        if (containsTransaction(account, transaction)) {
            throw new TransactionAlreadyExistException("This transaction already exists in this account");
        }
        if (transaction instanceof Transfer) {
            if (transaction.getAmount() < 0) {
                throw new TransactionAttributeException("Negative transfers are illegal.");
            }
        } else if (transaction instanceof Payment) {
            ((Payment) transaction).setIncomingInterest(incomingInterest);
            ((Payment) transaction).setOutgoingInterest(outgoingInterest);
        }
        accountsToTransactions.get(account).add(transaction);
        writeAccount(account);
    }

    @Override
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException,
            TransactionDoesNotExistException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Cannot remove transaction from non-existing account");
        }
        if (!accountsToTransactions.get(account).contains(transaction)) {
            throw new TransactionDoesNotExistException("Cannot remove transaction. Transaction does not exist in this account");
        }
        accountsToTransactions.get(account).remove(transaction);
        writeAccount(account);
    }

    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        // can also add AccountDoesNotExistException

        List<Transaction> trList = accountsToTransactions.get(account);
        return trList.contains(transaction);

    }

    @Override
    public double getAccountBalance(String account) {
        double accountBalance = 0;
        for (Transaction t : accountsToTransactions.get(account)) {
            accountBalance += t.calculate();
        }
        return accountBalance;
    }

    @Override
    public List<Transaction> getTransactions(String account) {
        return accountsToTransactions.get(account);
    }

    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {

        if (asc) {
            accountsToTransactions.get(account).sort(PrivateBank.compareTransactionsAsc);
            return accountsToTransactions.get(account);
        }
        accountsToTransactions.get(account).sort(PrivateBank.compareTransactionsDesc);
        return accountsToTransactions.get(account);

    }

    /**
     * Comparator, which helps to sort elements in the list in ascending order
     */
    public static Comparator<Transaction> compareTransactionsAsc = (o1, o2) -> {
        double amount1 = o1.calculate();
        double amount2 = o2.calculate();

        return (int) (amount1 - amount2);
    };

    /**
     * Comparator, which helps to sort elements in the list in descending order
     */
    public static Comparator<Transaction> compareTransactionsDesc = (o1, o2) -> {
        double amount1 = o1.calculate();
        double amount2 = o2.calculate();

        return (int) (amount2 - amount1);
    };

    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> resultList = new ArrayList<>();

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

    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Cannot delete an account, which does not exist");
        }
        accountsToTransactions.remove(account);

        deleteJsonFile(account);
        writeAccount(account);
    }

    @Override
    public List<String> getAllAccounts() {
        return new ArrayList<>(accountsToTransactions.keySet());
    }


    /**
     * This method deserializes and reads all JSON files from the transactions directory and saves in
     * array list for further usage in the program
     *
     * @throws IOException                      if the file did not open, written or read correctly
     * @throws TransactionAlreadyExistException if transaction list has duplicates
     * @throws AccountAlreadyExistsException    if creating account already exists
     * @throws AccountDoesNotExistException     if the account, where we are adding does not exist
     * @throws TransactionAttributeException    if there is an incorrect input for transaction attribute
     */
    private void readAccounts() throws IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException {

        File directory = new File(directoryName);
        File[] list = directory.listFiles();
        if (list != null) {
            for (File file : list) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Transaction.class, new Serializer()).setPrettyPrinting()
                        .create();

                Reader reader = Files.newBufferedReader(Paths.get(file.getPath()));

                List<Transaction> transactionsList = gson.fromJson(reader, new TypeToken<List<Transaction>>() {
                }.getType());

                String nameStr = file.getName();
                String accName = nameStr.substring(0, nameStr.length() - 5);

                createAccount(accName, transactionsList);
                //transactionsList.forEach(System.out::println);

                reader.close();
            }
        }

    }

    /**
     * Method serializes class objects and converts them to JSON file, saving
     * them in transactions directory, which makes them persistent
     *
     * @param account whose transactions are to be serialized
     * @throws IOException thrown, if there are problems with input or output of the file
     */
    private void writeAccount(String account) throws IOException {
        if (accountsToTransactions.containsKey(account)) {
            String filePath = directoryName + account + ".json";
            File myObj = new File(filePath);
            Writer writer = new FileWriter(myObj);
            List<String> transacList = new ArrayList();

            for (Transaction t : getTransactions(account)) {
                Gson gson = new GsonBuilder().registerTypeAdapter(t.getClass(), new Serializer())
                        .setPrettyPrinting().create();
                transacList.add(gson.toJson(t));
            }

            writer.write(transacList.toString());

            writer.close();
        }
    }

    private void deleteJsonFile(String account) {
        File file = new File("src/main/java/headquarters/bank/transactions/" + account + ".json");
        if (file.exists()) {
            file.delete();
        }
    }

}
