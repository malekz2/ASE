package pizzaApp;

public class OrderEntity {
    private int id;
    private String username;
    private String size;
    private String crust;
    private String toppings; // comma-separated
    private int quantity;
    private double subtotal, tax, total;
    private OrderStatus status;
    private String createdAt;

    // getters/setters omitted for brevity — generate all
    // ...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getCrust() { return crust; }
    public void setCrust(String crust) { this.crust = crust; }
    public String getToppings() { return toppings; }
    public void setToppings(String toppings) { this.toppings = toppings; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
