package billCoinGenerator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * @author malekzuhdi
 */
public class BillCoinGeneratorController {

    @FXML
    private TextField amountField;

    @FXML
    private Button calculateButton;

    @FXML
    private Label billAmountLabel, coinAmountLabel;

    @FXML
    private Label tenDollarLabel, fiveDollarLabel, oneDollarLabel;

    @FXML
    private Label quartersLabel, dimesLabel, nickelsLabel, penniesLabel;

    @FXML
    protected void onCalculateClicked() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            int totalDollars = (int) amount;
            int totalCents = (int) Math.round((amount - totalDollars) * 100);

            billAmountLabel.setText("Total bill amount: " + totalDollars + " USD");
            coinAmountLabel.setText("Total coins in cents: " + totalCents + " cents");

            // Bill breakdown
            int tens = totalDollars / 10;
            totalDollars %= 10;

            int fives = totalDollars / 5;
            totalDollars %= 5;

            int ones = totalDollars;

            tenDollarLabel.setText("Number 10 dollar bills: " + tens);
            fiveDollarLabel.setText("Number 5 dollar bills: " + fives);
            oneDollarLabel.setText("Number 1 dollar bills: " + ones);

            // Coin breakdown
            int quarters = totalCents / 25;
            totalCents %= 25;

            int dimes = totalCents / 10;
            totalCents %= 10;

            int nickels = totalCents / 5;
            totalCents %= 5;

            int pennies = totalCents;

            quartersLabel.setText("Number of quarters: " + quarters);
            dimesLabel.setText("Number of dimes: " + dimes);
            nickelsLabel.setText("Number of nickels: " + nickels);
            penniesLabel.setText("Number of pennies: " + pennies);

        } catch (NumberFormatException e) {
            billAmountLabel.setText("Invalid input! Please enter a number.");
            coinAmountLabel.setText("");
            tenDollarLabel.setText("");
            fiveDollarLabel.setText("");
            oneDollarLabel.setText("");
            quartersLabel.setText("");
            dimesLabel.setText("");
            nickelsLabel.setText("");
            penniesLabel.setText("");
        }
    }
}
