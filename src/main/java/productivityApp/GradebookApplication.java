package productivityApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GradebookApplication extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        try { DB.init(); } catch (Exception e) { e.printStackTrace(); }
        showLogin();
    }

    public static void showLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(GradebookApplication.class.getResource("login-view.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public static void showSignup() throws IOException {
        FXMLLoader loader = new FXMLLoader(GradebookApplication.class.getResource("signup-view.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Signup");
        primaryStage.show();
    }

    public static void showDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(GradebookApplication.class.getResource("dashboard-view.fxml"));
        primaryStage.setScene(new Scene(loader.load(), 1000, 700));
        primaryStage.setTitle("Productivity Dashboard");
        primaryStage.show();
    }
}
