package gradebook;

import app.SceneNavigator;
import db.Database;
import gradebook.models.User;
import gradebook.models.UserRole;
import javafx.scene.control.Alert;

import app.SceneNavigator;
import db.Database;
import javafx.scene.control.Alert;

import java.sql.*;

public class UserService {

    public boolean addUser(String username, String password, boolean asProducer) {
        try (Connection conn = DriverManager.getConnection(Database.url())) {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, asProducer ? "PRODUCER" : "CUSTOMER");
                ps.executeUpdate();
            }
            new Alert(Alert.AlertType.INFORMATION) {{
                setTitle("Success"); setHeaderText(null); setContentText("Signup successful");
            }}.showAndWait();
            SceneNavigator.goToLogin();
            return true;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR) {{
                setTitle("Database Error"); setHeaderText(null); setContentText("Could not register: " + e.getMessage());
            }}.showAndWait();
            return false;
        }
    }

    public User loginUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(Database.url())) {
            String sql = "SELECT id, username, role FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String un = rs.getString("username");
                        UserRole role = UserRole.valueOf(rs.getString("role"));
                        return new User(id, un, role);
                    }
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR) {{ setTitle("Database Error"); setHeaderText(null); setContentText(e.getMessage()); }}.showAndWait();
        }
        return null;
    }
}
