package headquarters;

import headquarters.bank.PrivateBank;
import headquarters.bank.exceptions.AccountAlreadyExistsException;
import headquarters.bank.exceptions.AccountDoesNotExistException;
import headquarters.bank.exceptions.TransactionAlreadyExistException;
import headquarters.bank.exceptions.TransactionAttributeException;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller of the main page of the bank, which contains list of account names
 * with functionalities of deleting, creating a new account and choosing an account to see their
 * transactions
 */
public class MainController implements Initializable {
    private List<String> accounts = Mainview.p.getAllAccounts();
    public static String accountName;

    @FXML
    private ListView<String> mainListView;

    private Parent root;

    public MainController() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainListView.getItems().addAll(accounts);

        mainListView.setCellFactory(lv -> {

            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

            // Context menu
            MenuItem chooseItem = new MenuItem();
            chooseItem.textProperty().bind(Bindings.format("Auswählen \"%s\"", cell.itemProperty()));

            chooseItem.setOnAction(event -> {
                accountName = cell.getItem();
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("accountpage.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                AccountController accountController = loader.getController();
                accountController.setAccountName(accountName);
                accountController.setAccountBalance(Mainview.p, accountName);

                Stage s = Mainview.stage;
                Scene scene = new Scene(root);
                s.setScene(scene);
                s.show();

            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Löschen \"%s\"", cell.itemProperty()));

            // DeleteAccount Dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            //Setting the title
            dialog.setTitle("Account löschen?");
            ButtonType ok = ButtonType.OK;
            ButtonType cancel = ButtonType.CANCEL;
            //Setting the content of the dialog
            dialog.contentTextProperty().bind(Bindings.format("Wollen Sie den Account \"%s\" wirklich löschen?", cell.itemProperty()));
            //Adding buttons to the dialog pane
            dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

            deleteItem.setOnAction(event -> {
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.get() == ButtonType.OK) {
                    String item = cell.getItem();
                    try {
                        Mainview.p.deleteAccount(item);
                        mainListView.getItems().remove(item);
                    } catch (AccountDoesNotExistException | IOException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Fehler");
                        alert.setHeaderText("Es ist ein Fehler aufgetreten");
                        alert.setContentText(ex.getMessage());

                        alert.showAndWait();
                    }
                } else {
                    dialog.close();
                }

            });
            contextMenu.getItems().addAll(chooseItem, deleteItem);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });

    }


    public void createAccount(ActionEvent actionEvent) {

        TextInputDialog inputDialog = new TextInputDialog();

        inputDialog.setHeaderText("Neuen Account erstellen");
        inputDialog.initModality(Modality.APPLICATION_MODAL);

        ButtonType ok = inputDialog.getDialogPane().getButtonTypes().get(0);
        Button btn = (Button) inputDialog.getDialogPane().lookupButton(ok);

        btn.setOnAction(e -> {

            if (inputDialog.getResult().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Fehler");
                alert.setHeaderText("Es ist ein Fehler aufgetreten");
                alert.setContentText("Eingabefeld ist leer");
                alert.showAndWait();
            } else {
                String accName = inputDialog.getResult();
                try {
                    Mainview.p.createAccount(accName);
                    mainListView.getItems().add(accName);
                } catch (AccountAlreadyExistsException | IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Es ist ein Fehler aufgetreten");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            }
        });

        inputDialog.showAndWait();
    }
}
