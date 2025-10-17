package productivityApp;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;

public class SignUpController {

    @FXML private TextField idField;
    @FXML private TextField usernameid;
    @FXML private TextField gradeLevelField;
    @FXML private DatePicker birthday;
    @FXML private PasswordField passid;
    @FXML private PasswordField cpassid;
    @FXML private Button btnRegister;
    @FXML private Hyperlink lblLogin;

    private static final String DB_URL =
            "jdbc:sqlite:/Users/malekzuhdi/IdeaProjects/ASE/src/main/resources/databases/studentInfo";
    private final UserService userService = new UserService();
    @FXML
    private void lblClickRegister() {
        String idText = idField.getText();
        String username = usernameid.getText();
        String gradeText = gradeLevelField.getText();
        LocalDate birthDate = birthday.getValue();
        String password = passid.getText();
        String confirmPassword = cpassid.getText();

        // Validation
        if (idText.isEmpty() || username.isEmpty() || gradeText.isEmpty() ||
                birthDate == null || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Error", "Passwords do not match.");
            return;
        }

        boolean success = userService.addUser(idText, username, gradeText, birthDate, password);

    }

    @FXML
    private void lblClickLogin() throws IOException {
        System.out.println("Redirecting to login...");
        GradebookApplication.showLogin();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
