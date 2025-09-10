package pizzaApp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller for Pizza App
 */
public class pizzaAppController {

    // Size radio buttons
    @FXML private RadioButton radSmall;
    @FXML private RadioButton radMedium;
    @FXML private RadioButton radLarge;
    @FXML private ToggleGroup sizeGroup;

    // Crust selection
    @FXML private ComboBox<String> cmbCrust;

    // Toppings
    @FXML private CheckBox chkPepperoni;
    @FXML private CheckBox chkMushrooms;
    @FXML private CheckBox chkOnions;
    @FXML private CheckBox chkOlives;
    @FXML private CheckBox chkExtraCheese;
    @FXML private CheckBox chkChicken;
    @FXML private CheckBox chkPineapple;

    // Quantity
    @FXML private Spinner<Integer> spnQty;

    // Buttons
    @FXML private Button btnOrder;
    @FXML private Button btnClear;

    // Output
    @FXML private TextArea txtResult;

    // Small decorative image (optional)
    @FXML private ImageView imgPizza;

    // Model
    private PizzaOrder currentOrder;

    @FXML
    private void initialize() {
        // Initialize model
        currentOrder = new PizzaOrder();

        // Setup size toggle defaults
        if (sizeGroup != null && radMedium != null) {
            sizeGroup.selectToggle(radMedium);
        }

        // Setup crust options
        if (cmbCrust != null) {
            cmbCrust.getItems().addAll("Thin Crust", "Hand-Tossed (+$1.00)", "Stuffed Crust (+$3.00)");
            cmbCrust.getSelectionModel().selectFirst();
        }

        // Spinner for quantity
        if (spnQty != null) {
            SpinnerValueFactory<Integer> factory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1);
            spnQty.setValueFactory(factory);
            spnQty.valueProperty().addListener((obs, oldV, newV) -> {
                currentOrder.setQuantity(newV);
                updateSummaryAndPrice();
            });
        }

        // Wire radio group listener
        if (sizeGroup != null) {
            radMedium.setSelected(true); // default
            updateSizeFromToggle(sizeGroup.getSelectedToggle());

            sizeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                updateSizeFromToggle(newToggle);
                updateSummaryAndPrice();
            });
        }

        // Toppings listeners
        List<CheckBox> toppings = List.of(chkPepperoni, chkMushrooms, chkOnions, chkOlives, chkExtraCheese, chkChicken, chkPineapple);
        for (CheckBox cb : toppings) {
            if (cb != null) {
                cb.selectedProperty().addListener((obs, oldV, newV) -> {
                    updateToppingsFromCheckboxes();
                    updateSummaryAndPrice();
                });
            }
        }

        // Crust selection listener
        if (cmbCrust != null) {
            cmbCrust.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
                currentOrder.setCrust(newV);
                updateSummaryAndPrice();
            });
        }

        // Buttons
        if (btnOrder != null) {
            btnOrder.setOnAction(e -> placeOrder());
        }
        if (btnClear != null) {
            btnClear.setOnAction(e -> clearOrder());
        }

        // Optional image: small pizza icon if ImageView present and resource exists
        try {
            if (imgPizza != null) {
                Image img = new Image(getClass().getResourceAsStream("/pizzaApp/pizza_icon.png"));
                imgPizza.setImage(img);
                imgPizza.setPreserveRatio(true);
                imgPizza.setFitHeight(64);
            }
        } catch (Exception ignored) {}

        // Initialize quantity & toppings & crust in model
        currentOrder.setQuantity(spnQty != null ? spnQty.getValue() : 1);
        currentOrder.setCrust(cmbCrust != null ? cmbCrust.getValue() : "Thin Crust");
        updateToppingsFromCheckboxes();
        updateSummaryAndPrice();
    }

    private void updateSizeFromToggle(Toggle selected) {
        if (selected == radSmall) {
            currentOrder.setSize(PizzaOrder.Size.SMALL);
        } else if (selected == radMedium) {
            currentOrder.setSize(PizzaOrder.Size.MEDIUM);
        } else if (selected == radLarge) {
            currentOrder.setSize(PizzaOrder.Size.LARGE);
        }
    }

    private void updateToppingsFromCheckboxes() {
        List<String> top = new ArrayList<>();
        if (chkPepperoni != null && chkPepperoni.isSelected()) top.add("Pepperoni");
        if (chkMushrooms != null && chkMushrooms.isSelected()) top.add("Mushrooms");
        if (chkOnions != null && chkOnions.isSelected()) top.add("Onions");
        if (chkOlives != null && chkOlives.isSelected()) top.add("Olives");
        if (chkExtraCheese != null && chkExtraCheese.isSelected()) top.add("Extra Cheese");
        if (chkChicken != null && chkChicken.isSelected()) top.add("Chicken");
        if (chkPineapple != null && chkPineapple.isSelected()) top.add("Pineapple");
        currentOrder.setToppings(top);
    }

    private void updateSummaryAndPrice() {
        if (txtResult == null || currentOrder == null) return;

        double subtotal = currentOrder.calculateSubtotal();
        double tax = currentOrder.calculateTax(subtotal);
        double total = subtotal + tax;

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);

        StringBuilder sb = new StringBuilder();
        sb.append("🍕 Pizza Order Preview\n");
        sb.append("-------------------------------\n");
        sb.append("Size: ").append(currentOrder.getSize().toString()).append("\n");
        sb.append("Crust: ").append(currentOrder.getCrust()).append("\n");
        sb.append("Toppings: ").append(currentOrder.getToppings().isEmpty() ? "None" : String.join(", ", currentOrder.getToppings())).append("\n");
        sb.append("Quantity: ").append(currentOrder.getQuantity()).append("\n\n");
        sb.append(String.format("Subtotal: %s\n", nf.format(subtotal)));
        sb.append(String.format("Tax (%.0f%%): %s\n", currentOrder.getTaxRate()*100, nf.format(tax)));
        sb.append(String.format("Total: %s\n", nf.format(total)));
        sb.append("\n(Click ORDER to finalize — this demo does not send the order anywhere.)");

        txtResult.setText(sb.toString());
    }

    private void placeOrder() {
        // final confirmation dialog
        double subtotal = currentOrder.calculateSubtotal();
        double tax = currentOrder.calculateTax(subtotal);
        double total = subtotal + tax;
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Order");
        alert.setHeaderText("Place your pizza order?");
        alert.setContentText("Total: " + nf.format(total) + "\n\nPress OK to confirm.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Pretend to place order: append a friendly message and clear quantity to 1
                StringBuilder receipt = new StringBuilder();
                receipt.append("✅ Order Placed!\n");
                receipt.append("---------------------\n");
                receipt.append(txtResult.getText()).append("\n\n");
                receipt.append("Thank you! Your pizza will be prepared shortly. 🍕");

                // Show receipt in information dialog
                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("Order Complete");
                done.setHeaderText("Order successfully placed");
                TextArea ta = new TextArea(receipt.toString());
                ta.setEditable(false);
                ta.setWrapText(true);
                ta.setPrefRowCount(10);
                done.getDialogPane().setContent(ta);
                done.showAndWait();

                // Reset quantity to 1
                if (spnQty != null) spnQty.getValueFactory().setValue(1);
                // Optionally clear toppings (comment out if you prefer keep selection)
                // clearToppings();
            }
        });
    }

    private void clearOrder() {
        if (spnQty != null) spnQty.getValueFactory().setValue(1);
        if (sizeGroup != null) sizeGroup.selectToggle(radMedium);
        if (cmbCrust != null) cmbCrust.getSelectionModel().selectFirst();
        if (chkPepperoni != null) chkPepperoni.setSelected(false);
        if (chkMushrooms != null) chkMushrooms.setSelected(false);
        if (chkOnions != null) chkOnions.setSelected(false);
        if (chkOlives != null) chkOlives.setSelected(false);
        if (chkExtraCheese != null) chkExtraCheese.setSelected(false);
        if (chkChicken != null) chkChicken.setSelected(false);
        if (chkPineapple != null) chkPineapple.setSelected(false);

        updateToppingsFromCheckboxes();
        updateSummaryAndPrice();
    }

    private void clearToppings() {
        if (chkPepperoni != null) chkPepperoni.setSelected(false);
        if (chkMushrooms != null) chkMushrooms.setSelected(false);
        if (chkOnions != null) chkOnions.setSelected(false);
        if (chkOlives != null) chkOlives.setSelected(false);
        if (chkExtraCheese != null) chkExtraCheese.setSelected(false);
        if (chkChicken != null) chkChicken.setSelected(false);
        if (chkPineapple != null) chkPineapple.setSelected(false);
    }
}
