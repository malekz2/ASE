package pizzaApp;

import db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    public int placeOrder(String username, PizzaOrder order) {
        double subtotal = order.calculateSubtotal();
        double tax = order.calculateTax(subtotal);
        double total = subtotal + tax;

        String toppings = String.join(",", order.getToppings());
        String size = order.getSize().name();
        String crust = order.getCrust();

        try (Connection conn = DriverManager.getConnection(Database.url())) {
            String sql = """
                INSERT INTO orders(username, size, crust, toppings, quantity, subtotal, tax, total, status)
                VALUES(?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE')
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                ps.setString(2, size);
                ps.setString(3, crust);
                ps.setString(4, toppings);
                ps.setInt(5, order.getQuantity());
                ps.setDouble(6, subtotal);
                ps.setDouble(7, tax);
                ps.setDouble(8, total);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to place order", e);
        }
        return -1;
    }

    public List<OrderEntity> listActiveOrders() {
        List<OrderEntity> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(Database.url());
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM orders WHERE status = 'ACTIVE' ORDER BY created_at ASC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void updateStatus(int orderId, OrderStatus status) {
        try (Connection conn = DriverManager.getConnection(Database.url());
             PreparedStatement ps = conn.prepareStatement("UPDATE orders SET status=? WHERE id=?")) {
            ps.setString(1, status.name());
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update status", e);
        }
    }

    private OrderEntity map(ResultSet rs) throws SQLException {
        OrderEntity o = new OrderEntity();
        o.setId(rs.getInt("id"));
        o.setUsername(rs.getString("username"));
        o.setSize(rs.getString("size"));
        o.setCrust(rs.getString("crust"));
        o.setToppings(rs.getString("toppings"));
        o.setQuantity(rs.getInt("quantity"));
        o.setSubtotal(rs.getDouble("subtotal"));
        o.setTax(rs.getDouble("tax"));
        o.setTotal(rs.getDouble("total"));
        o.setStatus(OrderStatus.valueOf(rs.getString("status")));
        o.setCreatedAt(rs.getString("created_at"));
        return o;
    }
}
