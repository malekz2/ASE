package CollegeApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
* @author malekzuhdi on 03/09/2025
*/public class CollegeAppApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Use absolute path (leading slash) relative to classpath root
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CollegeApp/collegeapp_view.fxml"));
        if (fxmlLoader.getLocation() == null) {
            throw new IOException("FXML not found: /CollegeApp/collegeapp_view.fxml");
        }
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("College Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
