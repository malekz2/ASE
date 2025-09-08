package piggyBank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author malekzuhdi on 01/09/2025
 */
public class PiggyBankApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PiggyBankApplication.class.getResource("piggybank-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        scene.getStylesheets().add(PiggyBankApplication.class.getResource("piggybank.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Piggy Bank");
        primaryStage.show();
    }
}
