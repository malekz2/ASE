package gradebook;

import app.SceneNavigator;
import app.SecurityContext;
import gradebook.models.User;
import gradebook.models.UserRole;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField usernameid;
    @FXML private PasswordField passid;

    private static final UserService userService = new UserService();

    @FXML
    private void lblCklckLogin() {
        String username = usernameid.getText();
        String password = passid.getText();
        if (username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR) {{ setTitle("Error"); setHeaderText(null); setContentText("Please fill all the fields"); }}.showAndWait();
            return;
        }

        User user = userService.loginUser(username, password);
        if (user != null) {
            SecurityContext.set(user);
            new Alert(Alert.AlertType.INFORMATION) {{ setTitle("Success"); setHeaderText(null); setContentText("Login Successful as " + user.getRole()); }}.showAndWait();
            try {
                if (user.getRole() == UserRole.CUSTOMER) {
                    SceneNavigator.goToCustomerOrder();
                } else {
                    SceneNavigator.goToProducerBoard();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR) {{ setTitle("Navigation Error"); setHeaderText(null); setContentText(e.getMessage()); }}.showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.ERROR) {{ setTitle("Error"); setHeaderText(null); setContentText("Login Failed"); }}.showAndWait();
        }
    }

    @FXML
    private void lblClckSignUp() {
        try {
            SceneNavigator.goToSignup();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR) {{ setTitle("Navigation Error"); setHeaderText(null); setContentText(e.getMessage()); }}.showAndWait();
        }
    }
}
