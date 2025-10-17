package productivityApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class JournalService {

    public void addEntry(int userId, LocalDate date, String content) {
        String sql = "INSERT INTO journal_entries(user_id, entry_date, content) VALUES(?,?,?)";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, date.toString());
            ps.setString(3, content);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public ObservableList<JournalEntry> listEntries(int userId) {
        ObservableList<JournalEntry> items = FXCollections.observableArrayList();
        String sql = "SELECT id, user_id, entry_date, content FROM journal_entries WHERE user_id=? ORDER BY entry_date DESC, id DESC";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new JournalEntry(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            LocalDate.parse(rs.getString("entry_date")),
                            rs.getString("content")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return items;
    }
}
