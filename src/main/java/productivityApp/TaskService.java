package productivityApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class TaskService {

    public ObservableList<Task> listTasks(int userId) {
        ObservableList<Task> items = FXCollections.observableArrayList();
        String sql = "SELECT id, user_id, title, due_date, completed FROM tasks WHERE user_id=? ORDER BY completed, due_date IS NULL, due_date";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate due = rs.getString("due_date") == null ? null : LocalDate.parse(rs.getString("due_date"));
                    items.add(new Task(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("title"),
                            due,
                            rs.getInt("completed")==1
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return items;
    }

    public void addTask(int userId, String title, LocalDate dueDate) {
        String sql = "INSERT INTO tasks(user_id, title, due_date) VALUES(?,?,?)";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, dueDate==null ? null : dueDate.toString());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteTask(int taskId) {
        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM tasks WHERE id=?")) {
            ps.setInt(1, taskId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void setCompleted(int taskId, boolean completed) {
        String sql = "UPDATE tasks SET completed=?, completed_at=CASE WHEN ?=1 THEN DATE('now') ELSE NULL END WHERE id=?";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, completed ? 1 : 0);
            ps.setInt(2, completed ? 1 : 0);
            ps.setInt(3, taskId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /** count of tasks completed per day for last N days (inclusive, ending today) */
    public Map<LocalDate,Integer> tasksCompletedLastNDays(int userId, int days) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(days-1L);
        Map<LocalDate,Integer> counts = new LinkedHashMap<>();
        for (int i=0; i<days; i++) counts.put(start.plusDays(i), 0);

        String sql = "SELECT completed_at FROM tasks WHERE user_id=? AND completed=1 AND date(completed_at) BETWEEN ? AND ?";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, start.toString());
            ps.setString(3, end.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String d = rs.getString("completed_at");
                    if (d != null) {
                        LocalDate ld = LocalDate.parse(d);
                        counts.computeIfPresent(ld, (k, v) -> v+1);
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return counts;
    }
}
