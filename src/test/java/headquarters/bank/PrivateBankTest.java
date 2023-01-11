package headquarters.bank;

import headquarters.bank.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrivateBankTest {

    private PrivateBank privateBank;
    private static List<Transaction> transactionList = new ArrayList<>();
    private static List<Transaction> duplicateList = new ArrayList<>();
    static Payment p1, p2;

    @BeforeAll
    static void beforeAll() throws TransactionAttributeException {
        p1 = new Payment("20.07.2020", 123, "mensa", 0.13, 0.45);
        p2 = new Payment(p1);
        transactionList.add(new Payment("22.07.2012", -76, "sport", 0.3, 0.3));
        transactionList.add(new Payment("21.07.2022", 5054, "gehalt", 0.23, 0.12));
        transactionList.add(new Payment("25.07.2022", 43, "date", 0.13, 0.132));
        transactionList.add(new Payment("26.07.2022", -2, "sport", 0.5, 0.12));
        transactionList.add(new IncomingTransfer("26.04.2021", 15, "coffee", "Alice", "Bob"));
        transactionList.add(new OutgoingTransfer("14.08.2022", 6, "coffee", "Bob", "Alice"));
        transactionList.add(p1);
        duplicateList.add(p1);
        duplicateList.add(p2);
    }

    @BeforeEach
    void setUp() throws TransactionAttributeException, IOException, AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException {
        privateBank = new PrivateBank("Sparkasse", 0.5, 0.1);
        privateBank.createAccount("Eve");
        privateBank.createAccount("Diogenes", transactionList);
    }

    @AfterEach
    void tearDown() {
        File directory = new File(privateBank.directoryName);
        File[] list = directory.listFiles();
        if (list != null) {
            for (File file : list) {
                file.delete();
            }
        }
    }

    @Test
    void testEquals() throws TransactionAttributeException, IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException {
        PrivateBank pB1 = new PrivateBank("DB", 0.3, 0.2);
        PrivateBank pB2 = new PrivateBank(pB1);
        assertTrue(pB1.equals(pB2));
    }

    @Test
    void testToString() {
        String output = "PrivateBank: name=" + privateBank.getName() + ", incomingInterest=" + privateBank.getIncomingInterest() + ", outgoingInterest=" + privateBank.getOutgoingInterest();
        assertEquals(output, privateBank.toString());
    }

    @Test
    void createAccountTest() {
        assertNotNull(privateBank);
        assertDoesNotThrow(() -> {
            privateBank.createAccount("Adam");
        });
        assertTrue(privateBank.getTransactions("Adam").isEmpty());
        assertThrows(AccountAlreadyExistsException.class, () -> {
            privateBank.createAccount("Adam");
        });
    }

    @Test
    void testCreateAccountWithTransactions() throws AccountAlreadyExistsException, IOException, TransactionAlreadyExistException, TransactionAttributeException, AccountDoesNotExistException {
        assertNotNull(privateBank);
        assertDoesNotThrow(() -> {
            privateBank.createAccount("Bob", transactionList);
        });
        assertFalse(privateBank.getTransactions("Bob").isEmpty());
        assertThrows(AccountAlreadyExistsException.class, () -> {
            privateBank.createAccount("Bob", transactionList);
        });
    }

    @Test
    void addTransaction() throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {
        assertEquals(privateBank.getTransactions("Eve").size(), 0);
        privateBank.addTransaction("Eve", p1);
        assertEquals(privateBank.getTransactions("Eve").size(), 1);
        assertEquals(privateBank.getTransactions("Eve").get(0).getDescription(), "mensa");
        assertEquals(privateBank.getTransactions("Eve").get(0).getAmount(), 123);
        assertEquals(privateBank.getTransactions("Eve").get(0).getDate(), "20.07.2020");
        assertThrows(AccountDoesNotExistException.class, () -> {
            privateBank.addTransaction("Elisa", p2);
        });
        assertThrows(TransactionAlreadyExistException.class, () -> {
            privateBank.addTransaction("Eve", p1);
        });
        assertThrows(TransactionAttributeException.class, () -> {
            privateBank.addTransaction("Eve", new OutgoingTransfer("26.04.2021", -15, "essen", "Eve", "Adam"));
        });
    }

    @Test
    void removeTransaction() throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {
        int trSize = privateBank.getTransactions("Diogenes").size();
        privateBank.removeTransaction("Diogenes", p1);
        assertEquals(trSize - 1, privateBank.getTransactions("Diogenes").size());
        assertThrows(AccountDoesNotExistException.class, () -> {
            privateBank.removeTransaction("Maria", new OutgoingTransfer("26.04.2021", 15, "essen", "Eve", "Adam"));
        });
        assertThrows(TransactionDoesNotExistException.class, () -> {
            privateBank.removeTransaction("Diogenes", new Transfer("26.04.2021", 32, "essen", "Eve", "Adam"));
        });
    }

    @Test
    void containsTransaction() {
        assertTrue(privateBank.containsTransaction("Diogenes", p1));
        assertFalse(privateBank.containsTransaction("Eve", p1));
    }

    @Test
    void getAccountBalance() {
        double accountBalance = 0;
        for (Transaction t : privateBank.getTransactions("Diogenes")) {
            accountBalance += t.calculate();
        }
        assertEquals(accountBalance, privateBank.getAccountBalance("Diogenes"));
    }

    @Test
    void getTransactions() {
        assertNotNull(privateBank.getTransactions("Diogenes"));
        assertTrue(privateBank.getTransactions("Eve").isEmpty());
    }

    @Test
    void getTransactionsSorted() {
        double calc = privateBank.getTransactions("Diogenes").get(0).calculate();
        List<Transaction> ascSorted = privateBank.getTransactionsSorted("Diogenes", true);
        for (Transaction t : privateBank.getTransactions("Diogenes")) {
            if (t.calculate() < calc) {
                calc = t.calculate();
            }
        }
        assertEquals(calc, ascSorted.get(0).calculate());
        List<Transaction> descSorted = privateBank.getTransactionsSorted("Diogenes", false);
        for (Transaction t : privateBank.getTransactions("Diogenes")) {
            if (t.calculate() > calc) {
                calc = t.calculate();
            }
        }
        assertEquals(calc, descSorted.get(0).calculate());
    }

    @Test
    void getTransactionsByType() {
        for (Transaction t : privateBank.getTransactionsByType("Diogenes", true)) {
            assertTrue(t.calculate() >= 0);
        }
        for (Transaction t : privateBank.getTransactionsByType("Diogenes", false)) {
            assertTrue(t.calculate() < 0);
        }
    }

}