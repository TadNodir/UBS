package headquarters;

import headquarters.bank.*;
import headquarters.bank.exceptions.AccountDoesNotExistException;
import headquarters.bank.exceptions.TransactionAlreadyExistException;
import headquarters.bank.exceptions.TransactionAttributeException;
import headquarters.bank.exceptions.TransactionDoesNotExistException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Account Controller represents the account page,
 * with functions like adding a new transaction, deleting one, seeing a current balance,
 * and also sorting list of transactions in ascending, descending order, but also with
 * a functions of showing only positive or negative transfers
 */
public class AccountController implements Initializable {

    @FXML
    private Label accountName;

    @FXML
    private Label accountBalance;

    @FXML
    private Label message;

    @FXML
    private ListView<Transaction> transactionsList;

    private MenuItem deleteItem = new MenuItem("Löschen");


    @FXML
    public void backToMain() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("mainpage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 618, 512);
        Stage stage = Mainview.stage;
        stage.setScene(scene);
        stage.show();
    }

    public void setAccountName(String account) {
        accountName.setText(account);
    }

    public void setAccountBalance(PrivateBank pb, String account) {
        accountBalance.setText("Account balance: " + pb.getAccountBalance(account));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactionsList.getItems().addAll(Mainview.p.getTransactions(MainController.accountName));
        ContextMenu contextMenu = new ContextMenu();

        deleteItem.setOnAction((event) -> {
            if (transactionsList.getSelectionModel().getSelectedItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Keine Transaktion ausgewählt");
                alert.setHeaderText("Bitte eine Transaktion mit rechtsclick auswählen");
                alert.showAndWait();
            } else {
                Dialog<ButtonType> dialog = new Dialog();
                ButtonType ok = ButtonType.OK;
                ButtonType cancel = ButtonType.CLOSE;
                dialog.setTitle("Transaktion löschen");
                dialog.setHeaderText("Wollen Sie diese Transaktion wirklich löschen?");
                dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Transaction item = transactionsList.getSelectionModel().getSelectedItem();
                    try {
                        Mainview.p.removeTransaction(MainController.accountName, item);
                        transactionsList.getItems().remove(item);
                    } catch (AccountDoesNotExistException | IOException | TransactionDoesNotExistException ex) {
                        Alert msg = new Alert(Alert.AlertType.ERROR);
                        msg.setTitle("Fehler");
                        msg.setHeaderText("Es ist ein Fehler aufgetreten");
                        msg.setContentText(ex.getMessage());

                        msg.showAndWait();
                    }
                    setAccountBalance(Mainview.p, MainController.accountName);
                } else {
                    dialog.close();
                }

            }
        });
        contextMenu.getItems().add(deleteItem);
        transactionsList.setContextMenu(contextMenu);

    }


    public void addTransactionAction(ActionEvent actionEvent) {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Transaktion hinzufügen");
        dialog.setHeaderText("Geben Sie bitte die Transaktionsdetails");
        dialog.setResizable(true);
        ChoiceBox<String> choices = new ChoiceBox<>();
        String[] trStr = {"Payment", "Transfer"};
        choices.getItems().addAll(trStr);
        HBox hbox = new HBox(choices);
        dialog.getDialogPane().setContent(hbox);
        dialog.getDialogPane().setMinSize(200, 300);


        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        Button close = (Button) closeButton;
        close.setOnAction(e -> {
            dialog.close();
        });

        Label date = new Label("Date: ");
        Label amount = new Label("Amount: ");
        Label description = new Label("Description: ");
        Label incInt = new Label("Incoming interest: ");
        Label outInt = new Label("Outgoing interest: ");
        Label sender = new Label("Sender: ");
        Label recipient = new Label("Recipient: ");
        TextField dateText = new TextField();
        TextField amountText = new TextField();
        TextField descriptionText = new TextField();
        TextField incIntText = new TextField();
        TextField outIntText = new TextField();
        TextField senderText = new TextField();
        TextField recipientText = new TextField();
        GridPane grid = new GridPane();
        grid.add(date, 1, 1);
        grid.add(dateText, 2, 1);
        grid.add(amount, 1, 2);
        grid.add(amountText, 2, 2);
        grid.add(description, 1, 3);
        grid.add(descriptionText, 2, 3);

        try {
            choices.setOnAction(eventAction -> {
                switch (choices.getValue()) {
                    case "Payment" -> {
                        grid.add(incInt, 1, 4);
                        grid.add(incIntText, 2, 4);
                        grid.add(outInt, 1, 5);
                        grid.add(outIntText, 2, 5);
                    }
                    case "Transfer" -> {
                        grid.add(sender, 1, 4);
                        grid.add(senderText, 2, 4);
                        grid.add(recipient, 1, 5);
                        grid.add(recipientText, 2, 5);
                    }
                }
                dialog.getDialogPane().setContent(grid);
                ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
            });


            Optional<Transaction> result = dialog.showAndWait();

            String[] inputPaymentValues = {dateText.getText(), descriptionText.getText(), amountText.getText(),
                    incIntText.getText(), outIntText.getText()};
            String[] inputTransferValues = {dateText.getText(), descriptionText.getText(), amountText.getText(),
                    senderText.getText(), recipientText.getText()};
            if (result.isPresent() && choices.getValue() != null) {
                if (choices.getValue().equals("Payment")) {
                    if (invalidInput(inputPaymentValues)) {
                        throw new TransactionAttributeException("Keine Eingabe beim Payment");
                    }
                    Payment p = new Payment(dateText.getText(),
                            Double.parseDouble(amountText.getText()), descriptionText.getText(),
                            Double.parseDouble(incIntText.getText()), Double.parseDouble(outIntText.getText()));
                    Mainview.p.addTransaction(accountName.getText(), p);
                    transactionsList.getItems().add(p);
                } else if (choices.getValue().equals("Transfer")) {
                    if (invalidInput(inputTransferValues)) {
                        throw new TransactionAttributeException("Keine Eingabe beim Transfer");
                    }
                    if (senderText.getText().equals(accountName.getText())) {
                        OutgoingTransfer oT = new OutgoingTransfer(dateText.getText(),
                                Double.parseDouble(amountText.getText()), descriptionText.getText(),
                                senderText.getText(), recipientText.getText());
                        Mainview.p.addTransaction(accountName.getText(), oT);
                        transactionsList.getItems().add(oT);
                    } else if (recipientText.getText().equals(accountName.getText())) {
                        IncomingTransfer iT = new IncomingTransfer(dateText.getText(),
                                Double.parseDouble(amountText.getText()), descriptionText.getText(),
                                senderText.getText(), recipientText.getText());
                        Mainview.p.addTransaction(accountName.getText(), iT);
                        transactionsList.getItems().add(iT);
                        message.setText("");
                    } else {
                        throw new TransactionAttributeException("Weder Incoming noch Outgoing Transfer");
                    }
                }
            }
            setAccountBalance(Mainview.p, accountName.getText());
        } catch (TransactionAlreadyExistException | AccountDoesNotExistException |
                TransactionAttributeException | NumberFormatException | IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Es ist ein Fehler aufgetreten");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }
    }


    private boolean invalidInput(String[] inputValues) {
        for (String s : inputValues) {
            if (s.isBlank()) {
                return true;
            }
        }
        return false;
    }

    public void ascend(ActionEvent actionEvent) {
        transactionsList.getItems().setAll(Mainview.p.getTransactionsSorted(MainController.accountName, true));
    }

    public void descend(ActionEvent actionEvent) {
        transactionsList.getItems().setAll(Mainview.p.getTransactionsSorted(MainController.accountName, false));
    }

    public void positive(ActionEvent actionEvent) {
        transactionsList.getItems().setAll(Mainview.p.getTransactionsByType(MainController.accountName, true));
    }

    public void negative(ActionEvent actionEvent) {
        transactionsList.getItems().setAll(Mainview.p.getTransactionsByType(MainController.accountName, false));
    }
}


