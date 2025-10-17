package productivityApp;

import java.sql.*;

public class DB {
    // Use your existing absolute path — same file as your users table
    public static final String DB_URL =
            "jdbc:sqlite:/Users/malekzuhdi/IdeaProjects/ASE/src/main/resources/databases/studentInfo";

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void init() throws SQLException {
        try (Connection c = get(); Statement s = c.createStatement()) {
            s.execute("PRAGMA foreign_keys=ON");

            // Tasks table
            s.execute("""
                CREATE TABLE IF NOT EXISTS tasks (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  user_id INTEGER NOT NULL,
                  title TEXT NOT NULL,
                  due_date TEXT,
                  completed INTEGER NOT NULL DEFAULT 0,
                  created_at TEXT NOT NULL DEFAULT (DATE('now')),
                  completed_at TEXT,
                  FOREIGN KEY(user_id) REFERENCES student_dg_tmp_dg_tmp(IDNumber) ON DELETE CASCADE
                )
            """);

            // Journal entries
            s.execute("""
                CREATE TABLE IF NOT EXISTS journal_entries (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  user_id INTEGER NOT NULL,
                  entry_date TEXT NOT NULL,
                  content TEXT NOT NULL,
                  created_at TEXT NOT NULL DEFAULT (CURRENT_TIMESTAMP),
                  FOREIGN KEY(user_id) REFERENCES student_dg_tmp_dg_tmp(IDNumber) ON DELETE CASCADE
                )
            """);
        }
    }
}
