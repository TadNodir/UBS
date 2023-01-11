package headquarters.bank;

import headquarters.bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    private Transfer transfer;

    @BeforeEach
    public void setUp() throws TransactionAttributeException {
        transfer = new Transfer("26.04.2021", 3, "coffee", "Alice", "Bob");
    }

    @Test
    void testEquals() throws TransactionAttributeException {
        Transfer transfer2 = new Transfer(transfer);
        assertTrue(transfer.equals(transfer2));
    }

    @Test
    void testToString() throws TransactionAttributeException {
        String output = "Transfer: Date= " + transfer.getDate() + ", amount= " + transfer.calculate() +
                ", description= " + transfer.getDescription() + ", sender= " + transfer.getSender() + ", recipient= " +
                transfer.getRecipient();
        assertEquals(output, transfer.toString());
    }

    @Test
    void calculate() throws TransactionAttributeException {
        transfer = new OutgoingTransfer("26.04.2021", 15, "coffee", "Alice", "Bob");
        assertEquals(-15, transfer.calculate());
        transfer = new IncomingTransfer("26.04.2021", 15, "coffee", "Alice", "Bob");
        assertEquals(15, transfer.calculate());
    }

    @Test
    void testTransactionAttributeExc() {
        assertThrows(TransactionAttributeException.class, () -> {
            transfer.setAmount(-5);
        });
        assertDoesNotThrow(() -> {
            transfer.setAmount(0);
        });
        assertDoesNotThrow(() -> {
            transfer.setAmount(100);
        });
    }
}