package pizzaApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
* @author malekzuhdi on 03/09/2025
*/public class pizzaAppApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Use absolute path (leading slash) relative to classpath root
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pizzaApp/pizzaapp-view.fxml"));
        if (fxmlLoader.getLocation() == null) {
            throw new IOException("FXML not found: /pizzaApp/pizzaapp-view.fxml");
        }
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Pizza Delivery!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
