package pizzaApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for a pizza order
 */
public class PizzaOrder {
    public enum Size { SMALL, MEDIUM, LARGE }

    private Size size = Size.MEDIUM;
    private String crust = "Thin Crust";
    private List<String> toppings = new ArrayList<>();
    private int quantity = 1;

    // Pricing rules (can be adjusted)
    private final double PRICE_SMALL = 8.00;
    private final double PRICE_MEDIUM = 10.00;
    private final double PRICE_LARGE = 12.00;
    private final double CRUST_HANDTOSSED_EXTRA = 1.00;
    private final double CRUST_STUFFED_EXTRA = 3.00;
    private final double TOPPING_PRICE = 0.75;
    private final double TAX_RATE = 0.05; // 5%

    public PizzaOrder() {}

    // getters & setters
    public Size getSize() { return size; }
    public void setSize(Size size) { this.size = size; }

    public String getCrust() { return crust; }
    public void setCrust(String crust) { this.crust = crust; }

    public List<String> getToppings() { return toppings; }
    public void setToppings(List<String> toppings) {
        this.toppings = toppings == null ? new ArrayList<>() : new ArrayList<>(toppings);
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = Math.max(1, quantity); }

    public double getTaxRate() { return TAX_RATE; }

    /**
     * Calculate subtotal (without tax) for the whole order (quantity included).
     */
    public double calculateSubtotal() {
        double base;
        if (size == Size.SMALL) base = PRICE_SMALL;
        else if (size == Size.MEDIUM) base = PRICE_MEDIUM;
        else base = PRICE_LARGE;

        double crustExtra = 0.0;
        if (crust != null) {
            if (crust.toLowerCase().contains("hand-tossed")) crustExtra = CRUST_HANDTOSSED_EXTRA;
            else if (crust.toLowerCase().contains("stuffed")) crustExtra = CRUST_STUFFED_EXTRA;
        }

        double toppingsTotal = (toppings == null ? 0 : toppings.size()) * TOPPING_PRICE;

        double singlePizza = base + crustExtra + toppingsTotal;
        double subtotal = singlePizza * Math.max(1, quantity);
        // Round to cents
        return Math.round(subtotal * 100.0) / 100.0;
    }

    public double calculateTax(double subtotal) {
        double tax = subtotal * TAX_RATE;
        return Math.round(tax * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "PizzaOrder{" +
                "size=" + size +
                ", crust='" + crust + '\'' +
                ", toppings=" + toppings +
                ", quantity=" + quantity +
                '}';
    }
}
