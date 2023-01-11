package headquarters.bank;

/**
 * Interface of the Bills, which to be calculated
 *
 * @author tadno: Nodirjon Tadjiev
 * Date: 19.10.22
 */
public interface CalculateBill {
    /**
     * Prototype of calculate class, which calculates the transaction amount with account of interest tax
     *
     * @return transaction amount with account of interest tax
     */
    double calculate();
}
