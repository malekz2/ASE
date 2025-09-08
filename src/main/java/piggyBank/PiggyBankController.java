package piggyBank;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PiggyBankController {

    @FXML
    private Label lblTotal;

    @FXML
    private TextField txtPennies, txtNickels, txtDimes, txtQuarters;

    private double total = 0.0;

    private static final double PENNY = 0.01;
    private static final double NICKEL = 0.05;
    private static final double DIME = 0.10;
    private static final double QUARTER = 0.25;

    @FXML
    private void initialize() {
        updateLabel();
    }

    public Label getLblTotal() {
        return lblTotal;
    }

    public void setLblTotal(Label lblTotal) {
        this.lblTotal = lblTotal;
    }

    public TextField getTxtPennies() {
        return txtPennies;
    }

    public void setTxtPennies(TextField txtPennies) {
        this.txtPennies = txtPennies;
    }

    public TextField getTxtNickels() {
        return txtNickels;
    }

    public void setTxtNickels(TextField txtNickels) {
        this.txtNickels = txtNickels;
    }

    public TextField getTxtDimes() {
        return txtDimes;
    }

    public void setTxtDimes(TextField txtDimes) {
        this.txtDimes = txtDimes;
    }

    public TextField getTxtQuarters() {
        return txtQuarters;
    }

    public void setTxtQuarters(TextField txtQuarters) {
        this.txtQuarters = txtQuarters;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @FXML
    private void addCoins() {
        try {
            int pennies = parseInput(txtPennies.getText());
            int nickels = parseInput(txtNickels.getText());
            int dimes = parseInput(txtDimes.getText());
            int quarters = parseInput(txtQuarters.getText());

            total += pennies * PENNY;
            total += nickels * NICKEL;
            total += dimes * DIME;
            total += quarters * QUARTER;

            updateLabel();
            clearInputs();
        } catch (NumberFormatException e) {
            lblTotal.setText("⚠️ Please enter valid numbers!");
        }
    }

    @FXML
    private void resetPiggyBank() {
        total = 0.0;
        updateLabel();
    }

    private void updateLabel() {
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    private void clearInputs() {
        txtPennies.clear();
        txtNickels.clear();
        txtDimes.clear();
        txtQuarters.clear();
    }

    private int parseInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(input.trim());
    }
}
