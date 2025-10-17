package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SceneNavigator {
    private static Stage primary;

    public static void init(Stage s) { primary = s; }

    private static void setStageScene(Scene scene, String title) {
        // Load your background image once to size the stage
        Image bg = new Image(SceneNavigator.class.getResourceAsStream("/pizza_bg.png"));

        primary.setScene(scene);
        primary.setTitle(title);

        // Match window size to the background image
        primary.setWidth(bg.getWidth()/2);
        primary.setHeight(bg.getHeight()/2);

        primary.setResizable(true);
        primary.show();
    }

    public static void goToLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/app/login-view.fxml"));
        Scene scene = new Scene(loader.load());
        setStageScene(scene, "Login");
    }

    public static void goToSignup() throws Exception {
        FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/app/signup-view.fxml"));
        Scene scene = new Scene(loader.load());
        setStageScene(scene, "Signup");
    }

    public static void goToCustomerOrder() throws Exception {
        FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/pizzaApp/pizzaapp-view.fxml"));
        Scene scene = new Scene(loader.load());
        setStageScene(scene, "Pizza Delivery!");
    }

    public static void goToProducerBoard() throws Exception {
        FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/pizzaApp/producer-view.fxml"));
        Scene scene = new Scene(loader.load());
        setStageScene(scene, "Producer Dashboard");
    }
}
