package textOrNumber;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller class for textOrNumber application.
 * Handles user input and determines whether it's a number or text.
 *
 * @author malekzuhdi
 */
public class textOrNumberController {

    @FXML
    private TextField inputField;

    @FXML
    private Button checkButton;

    @FXML
    private Label resultLabel;

    @FXML
    private void onCheckButtonClick() {
        String input = inputField.getText().trim();

        if (input.isEmpty()) {
            resultLabel.setText("⚠ Please enter something.");
            return;
        }

        if (isNumeric(input)) {
            resultLabel.setText(":) That’s a number.");
        } else {
            resultLabel.setText(":( That’s text.");
        }
    }

    /**
     * Utility method to check if a string is numeric.
     *
     * @param str string to check
     * @return true if numeric, false otherwise
     */
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str); // handles integers & decimals
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
