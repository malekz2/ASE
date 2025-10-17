package app;

import db.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Ensure DB and tables exist
        Database.bootstrap();

        // Router holds the primary stage
        SceneNavigator.init(stage);

        // Go to Login first
        SceneNavigator.goToLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
