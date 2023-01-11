package headquarters;

import headquarters.bank.PrivateBank;
import headquarters.bank.exceptions.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Mainview Class represents the launching applicational facilities of the fxml files,
 * in this case mainpage.fxml
 */
public class Mainview extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Instantiation of the private bank and stage to be able to transition and use their values in
     * another controllers
     */
    public static PrivateBank p;
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException {
        p = new PrivateBank("Sparkasse", 0.3, 0.2);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("mainpage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 618, 512);
        stage = primaryStage;
        stage.setScene(scene);
        stage.show();
    }
}
