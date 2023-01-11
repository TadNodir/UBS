package headquarters.bank;

import headquarters.bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment payment;

    @BeforeEach
    void setUp() throws TransactionAttributeException {
        payment = new Payment("22.07.2012", -76, "sport", 0.1, 0.5);
    }

    @Test
    void testEquals() throws TransactionAttributeException {
        Payment payment2 = new Payment(payment);
        assertTrue(payment.equals(payment2));
    }

    @Test
    void testToString() {
        String output = "Payment: Date= " + payment.getDate() + ", amount= " + payment.calculate() + ", description= " +
                payment.getDescription() + ", incoming interest= " + payment.getIncomingInterest() +
                ", outgoing interest= " + payment.getOutgoingInterest();
        assertEquals(output, payment.toString());
    }

    @Test
    void calculate() throws TransactionAttributeException {
        assertEquals(-114.0, payment.calculate());
        payment.setAmount(76);
        assertEquals(68.4, payment.calculate());
    }

    @Test
    void testTransactionAttributeExc() {
        assertThrows(TransactionAttributeException.class, () -> {
            payment.setOutgoingInterest(1.1);
        });
        assertThrows(TransactionAttributeException.class, () -> {
            payment.setOutgoingInterest(-1);
        });
        assertThrows(TransactionAttributeException.class, () -> {
            payment.setOutgoingInterest(-0.1);
        });
        assertThrows(TransactionAttributeException.class, () -> {
            payment.setIncomingInterest(1.1);
        });
        assertThrows(TransactionAttributeException.class, () -> {
            payment.setIncomingInterest(-0.1);
        });
        assertThrows(TransactionAttributeException.class, () -> {
            payment.setIncomingInterest(-1);
        });
        assertDoesNotThrow(() -> {
            payment.setIncomingInterest(0.5);
        });
        assertDoesNotThrow(() -> {
            payment.setOutgoingInterest(0.3);
        });
    }
}