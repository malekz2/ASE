package productivityApp;

import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class UserService {
    private static final String TABLE = "student_dg_tmp_dg_tmp";

    public boolean addUser(String idText, String username, String gradeText, LocalDate birthDate, String password) {
        try (Connection conn = DB.get()) {
            // optional: enforce unique username at app layer
            try (PreparedStatement chk = conn.prepareStatement("SELECT 1 FROM " + TABLE + " WHERE IDNumber=? OR username=?")) {
                chk.setInt(1, Integer.parseInt(idText));
                chk.setString(2, username);
                if (chk.executeQuery().next()) {
                    alertErr("Duplicate", "That Student ID or Username already exists.");
                    return false;
                }
            }
            String insertQuery = "INSERT INTO " + TABLE + " (IDNumber, username, GradeLevel, Birthday, Password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setInt(1, Integer.parseInt(idText));
                pstmt.setString(2, username);
                pstmt.setInt(3, Integer.parseInt(gradeText));
                pstmt.setString(4, birthDate.toString());
                pstmt.setString(5, password);
                pstmt.executeUpdate();
            }
            alertInfo("Success", "User registered.");
            return true;
        } catch (NumberFormatException e) {
            alertErr("Input Error", "ID and Grade Level must be numbers.");
            return false;
        } catch (SQLException e) {
            alertErr("Database Error", e.getMessage());
            return false;
        }
    }

    public Optional<User> loginUser(String username, String password) {
        String query = "SELECT IDNumber, username FROM " + TABLE + " WHERE username = ? AND password = ?";
        try (Connection conn = DB.get(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(rs.getInt("IDNumber"), rs.getString("username")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            alertErr("Database Error", e.getMessage());
            return Optional.empty();
        }
    }

    private void alertErr(String t, String m){ var a=new Alert(Alert.AlertType.ERROR); a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();}
    private void alertInfo(String t, String m){ var a=new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();}
}
