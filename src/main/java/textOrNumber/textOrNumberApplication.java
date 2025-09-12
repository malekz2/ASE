package textOrNumber;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author malekzuhdi on 01/09/2025
 */
public class textOrNumberApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(textOrNumberApplication.class.getResource("textOrNumber-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("text or number?");
        primaryStage.show();
    }
}
