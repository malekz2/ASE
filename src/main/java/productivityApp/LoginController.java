package productivityApp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameid;
    @FXML private PasswordField passid;

    private static final UserService userService = new UserService();

    @FXML
    private void lblCklckLogin(){
        String username = usernameid.getText();
        String password = passid.getText();
        if(username.isEmpty() || password.isEmpty()){
            alertErr("Please fill all the fields");
            return;
        }
        var userOpt = userService.loginUser(username, password);
        if(userOpt.isPresent()){
            Session.set(userOpt.get());
            try { GradebookApplication.showDashboard(); }
            catch (IOException e) { e.printStackTrace(); }
        } else {
            alertErr("Login failed. Check your credentials.");
        }
    }

    @FXML
    private void lblClckSignUp() throws IOException {
        GradebookApplication.showSignup();
    }

    private void alertErr(String msg){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
