package gradebook;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class SignUpController {

    // removed: idField

    @FXML private TextField usernameid;
    @FXML private TextField gradeLevelField;
    @FXML private DatePicker birthday;
    @FXML private PasswordField passid;
    @FXML private PasswordField cpassid;
    @FXML private CheckBox cbProducer;
    @FXML private Label lblDate;  // dynamic label: "Birthday" or "Opening Date"

    private final UserService userService = new UserService();

    @FXML
    private void initialize() {
        // default to customer wording
        setDateFieldForProducer(false);
    }

    @FXML
    private void onRoleToggled() {
        setDateFieldForProducer(cbProducer != null && cbProducer.isSelected());
    }

    private void setDateFieldForProducer(boolean producer) {
        if (lblDate != null) lblDate.setText(producer ? "Opening Date" : "Birthday");
        if (birthday != null) birthday.setPromptText(producer ? "Opening Date" : "Birthday");
    }

    @FXML
    private void lblClickRegister() {
        String username = usernameid.getText();
        String gradeText = gradeLevelField.getText();   // optional / unused for DB
        LocalDate date = birthday.getValue();           // birthday or opening date
        String password = passid.getText();
        String confirmPassword = cpassid.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in username and passwords.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Error", "Passwords do not match.");
            return;
        }

        boolean asProducer = cbProducer != null && cbProducer.isSelected();
        // We don’t store date/grade in the users table, so we just ignore them here.
        userService.addUser(username, password, asProducer);
    }

    @FXML
    private void lblClickLogin() {
        try {
            app.SceneNavigator.goToLogin();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
