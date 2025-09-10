package billCoinGenerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author malekzuhdi on 01/09/2025
 */
public class BillCoinGeneratorApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BillCoinGeneratorApplication.class.getResource("billCoinGenerator-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        //scene.getStylesheets().add(BillCoinGeneratorApplication.class.getResource("billCoin.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Bill Coin Generator");
        primaryStage.show();
    }
}
