package headquarters;

import headquarters.bank.*;
import headquarters.bank.exceptions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Main class serves as a runner class of the bank
 *
 * @author tadno: Nodirjon Tadjiev
 * Date: 19.10.22
 */
public class Main {

    public static void main(String[] args) throws TransactionAttributeException, IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException {

        try {
            /**
             * Creating a new Private bank object
             */
            PrivateBank pB = new PrivateBank("UBS", 0.4, 0.12);
            /**
             * Checking equals method at the beginning
             */
            System.out.println("Checking the equals method in the beginning");
            PrivateBank pB1 = new PrivateBank("UBS", 0.4, 0.12);
            System.out.println(pB.equals(pB1));
            /**
             * Creating account with only string and adding transactions
             */
            pB.createAccount("Daniel");
            pB.addTransaction("Daniel", new IncomingTransfer("10.08.2022", 1000, "guter zweck", "Alice", "Daniel"));
            pB.addTransaction("Daniel", new OutgoingTransfer("13.03.2021", 134, "mensa", "Daniel", "STW Aachen"));
            pB.addTransaction("Daniel", new OutgoingTransfer("16.08.2021", 65, "caffee", "Daniel", "Lulu"));
            pB.addTransaction("Daniel", new IncomingTransfer("11.05.2014", 2, "zug", "Bob", "Daniel"));
            pB.addTransaction("Daniel", new Payment("12.06.2012", -45, "essen", 0.5, 0.34));
            pB.addTransaction("Daniel", new Payment("22.07.2012", 54, "sport", 0.3, 0.12));
            System.out.println("+++++++ Daniel: ++++++");
            printTransactionList(pB.getTransactions("Daniel"));
            System.out.println("Checking Daniels's getAccountBalance:");
            System.out.println(pB.getAccountBalance("Daniel"));

            /**
             * Checking Private Bank alternative for accountBalance functionality
             */
            PrivateBankAlt pBA = new PrivateBankAlt("UBS", 0.4, 0.12);
            /**
             * Creating account with only string and adding transactions
             */
            pBA.createAccount("John");
            pBA.addTransaction("John", new IncomingTransfer("10.08.2022", 1000, "guter zweck", "Alice", "John"));
            pBA.addTransaction("John", new OutgoingTransfer("13.03.2021", 134, "mensa", "John", "STW Aachen"));
            pBA.addTransaction("John", new OutgoingTransfer("16.08.2021", 65, "caffee", "John", "Lulu"));
            pBA.addTransaction("John", new IncomingTransfer("11.05.2014", 2, "zug", "Bob", "John"));
            pBA.addTransaction("John", new Payment("12.06.2012", -45, "essen", 0.5, 0.34));
            pBA.addTransaction("John", new Payment("22.07.2012", 54, "sport", 0.3, 0.12));
            System.out.println("+++++++ John Private Bank Alt: ++++++");
            printTransactionList(pBA.getTransactions("John"));
            System.out.println("Checking John's getAccountBalance:");
            System.out.println(pBA.getAccountBalance("John"));

            /**
             * Creating account with string and list
             */
            List<Transaction> hrisTransactions = new ArrayList<>();
            hrisTransactions.add(new Payment("22.07.2012", -76, "sport", 0.3, 0.3));
            hrisTransactions.add(new Payment("21.07.2022", 5054, "gehalt", 0.23, 0.12));
            hrisTransactions.add(new Payment("25.07.2022", 43, "date", 0.13, 0.132));
            hrisTransactions.add(new Payment("26.07.2022", -2, "sport", 0.5, 0.12));
            hrisTransactions.add(new IncomingTransfer("26.04.2021", 15, "caffee", "Daniel", "Hris"));
            hrisTransactions.add(new OutgoingTransfer("14.08.2022", 6, "caffee", "Hris", "Lulu"));

            pB.createAccount("Hris", hrisTransactions);
            System.out.println("+++++++ Hris: ++++++");
            printTransactionList(pB.getTransactions("Hris"));

            /**
             * Checking contains transaction method
             */
            System.out.println("Daniel contains transaction? ");
            Transfer t = new OutgoingTransfer("16.08.2021", 65, "caffee", "Daniel", "Lulu");
            System.out.println(pB.containsTransaction("Daniel", t));

            /**
             * Checking remove transaction method
             */
            System.out.println("Hris removed transaction?");
            Payment p = new Payment("25.07.2022", 43, "date", 0.4, 0.12);
            pB.removeTransaction("Hris", p);
            printTransactionList(pB.getTransactions("Hris"));

            /**
             * Checking the sorting list algorithm with descending list
             */
            System.out.println("Checking the sorting list algorithm with descending list: ");
            printTransactionList(pB.getTransactionsSorted("Daniel", false));
            System.out.println("+++++++++++++++Now ascending++++++++++++");
            printTransactionList(pB.getTransactionsSorted("Daniel", true));

            /**
             * Checking the method of showing only positive or negative transactions from the list
             */
            System.out.println("Checking the method of showing only positive or negative transactions from the list: ");
            printTransactionList(pB.getTransactionsByType("Hris", false));
            System.out.println("+++++++++++++++Now positive+++++++++++");
            printTransactionList(pB.getTransactionsByType("Hris", true));

            /**
             * Checking getAccountBalance method
             */
            System.out.println("Checking Daniel's getAccountBalance:");
            System.out.println(pB.getAccountBalance("Daniel"));
            System.out.println("Checking Hris' getAccountBalance:");
            System.out.println(pB.getAccountBalance("Hris"));

            /**
             * Checking the equals method
             */
            System.out.println("Checking the equals method at the end: ");
            PrivateBank pB2 = new PrivateBank("UBS", 0.4, 0.12);
            System.out.println(pB.equals(pB2));

            System.out.println("++++++++++++++++++++++++++++++++++");

            pB.createAccount("Ikram");
            pB.addTransaction("Ikram", new IncomingTransfer("11.05.2021", 1200, "Lohn", "Alice", "Ikram"));
            pB.addTransaction("Ikram", new OutgoingTransfer("12.05.2021", 23, "Cafeteria", "Ikram", "STW Aachen"));
            pB.addTransaction("Ikram", new OutgoingTransfer("24.11.2022", 45, "Date", "Ikram", "Restaurant"));
            pB.addTransaction("Ikram", new IncomingTransfer("26.07.2022", 8, "Shawarma", "Bob", "Ikram"));
            pB.addTransaction("Ikram", new Payment("12.06.2012", -45, "Flugticket", 0.5, 0.34));
            pB.addTransaction("Ikram", new Payment("22.07.2012", -54, "Zugticket", 0.3, 0.12));


            pB.createAccount("Vammy");
            pB.addTransaction("Vammy", new IncomingTransfer("11.03.2022", 800, "Stipendium", "DAAD", "Vammy"));
            pB.addTransaction("Vammy", new OutgoingTransfer("18.10.2022", 2, "Caffee", "Vammy", "STW Aachen"));
            pB.addTransaction("Vammy", new IncomingTransfer("22.12.2022", 6, "Doener", "Bob", "Vammy"));
            pB.addTransaction("Vammy", new Payment("11.04.2022", -45, "Hotel", 0.5, 0.34));
            pB.addTransaction("Vammy", new Payment("12.09.2022", 54, "Aktiengewinn", 0.3, 0.12));

            System.out.println(pB.getAllAccounts());

//            pB.deleteAccount("Hris");
//
//            System.out.println(pB.getAllAccounts());

            /**
             * Catching all exceptions, which we defined in methods, which we used in try and printing out error messages
             */
        } catch (AccountAlreadyExistsException | AccountDoesNotExistException | TransactionAlreadyExistException |
                TransactionAttributeException | TransactionDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Utility method, which prints out transaction list
     *
     * @param transactions list to be printed out
     */
    public static void printTransactionList(List<Transaction> transactions) {
        for (Transaction tr : transactions) {
            System.out.println(tr);
        }
    }
}