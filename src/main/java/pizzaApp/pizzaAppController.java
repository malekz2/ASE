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

// add imports:
import app.SecurityContext;
import gradebook.models.User;
import javafx.application.Platform;
import app.SceneNavigator;
import app.SecurityContext;

// add field:

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

    @FXML private Button btnLogout;


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

    private final OrderService orderService = new OrderService();


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

        if (btnLogout != null) {
            btnLogout.setOnAction(e -> {
                SecurityContext.clear();
                try { SceneNavigator.goToLogin(); } catch (Exception ignored) {}
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
        Platform.runLater(() -> {
            User u = SecurityContext.get();
            boolean canOrder = (u != null && u.getRole().name().equals("CUSTOMER"));
            if (btnOrder != null) btnOrder.setDisable(!canOrder);
            if (!canOrder && txtResult != null) {
                txtResult.appendText("\n\nLogin as CUSTOMER to place orders.");
            }
        });
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

    private void placeOrder() {
        User u = SecurityContext.get();
        if (u == null) {
            new Alert(Alert.AlertType.ERROR) {{ setTitle("Not logged in"); setHeaderText(null); setContentText("Please log in as a CUSTOMER."); }}.showAndWait();
            return;
        }

        // Compute totals (existing code)
        double subtotal = currentOrder.calculateSubtotal();
        double tax = currentOrder.calculateTax(subtotal);
        double total = subtotal + tax;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Order");
        confirm.setHeaderText("Place your pizza order?");
        confirm.setContentText("Total: " + NumberFormat.getCurrencyInstance(Locale.US).format(total) + "\n\nPress OK to confirm.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // NEW: save to DB
                int id = orderService.placeOrder(u.getUsername(), currentOrder);

                StringBuilder receipt = new StringBuilder();
                receipt.append("✅ Order Placed! (Order #").append(id).append(")\n");
                receipt.append("---------------------\n");
                receipt.append(txtResult.getText()).append("\n\n");
                receipt.append("Thank you! Your pizza will be prepared shortly. 🍕");

                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("Order Complete");
                done.setHeaderText("Order successfully placed");
                TextArea ta = new TextArea(receipt.toString());
                ta.setEditable(false);
                ta.setWrapText(true);
                ta.setPrefRowCount(12);
                done.getDialogPane().setContent(ta);
                done.showAndWait();

                if (spnQty != null) spnQty.getValueFactory().setValue(1);
            }
        });
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
        sb.append("\n(Click ORDER to finalize)");

        txtResult.setText(sb.toString());
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
