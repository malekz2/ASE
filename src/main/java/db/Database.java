package db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class Database {
    // Relative DB path inside resources folder for dev; at runtime we keep it next to working dir
    private static final String DB_FILE = "pizza_shop.db";
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_FILE;

    public static String url() { return JDBC_URL; }

    public static void bootstrap() {
        // Create file if not exists (SQLite will create on connect)
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            try (Statement st = conn.createStatement()) {
                st.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      username TEXT NOT NULL UNIQUE,
                      password TEXT NOT NULL,
                      role TEXT NOT NULL CHECK(role IN ('CUSTOMER','PRODUCER'))
                    );
                """);
                st.execute("""
                    CREATE TABLE IF NOT EXISTS orders (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      username TEXT NOT NULL,
                      size TEXT NOT NULL,
                      crust TEXT NOT NULL,
                      toppings TEXT NOT NULL,     -- comma separated
                      quantity INTEGER NOT NULL,
                      subtotal REAL NOT NULL,
                      tax REAL NOT NULL,
                      total REAL NOT NULL,
                      status TEXT NOT NULL CHECK(status IN ('ACTIVE','COMPLETED','CANCELLED')),
                      created_at TEXT NOT NULL DEFAULT (datetime('now'))
                    );
                """);
            }

            // Seed a default producer if none exists
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT OR IGNORE INTO users (username, password, role) " +
                            "VALUES ('producer', 'producer123', 'PRODUCER')")) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB bootstrap failed", e);
        }
    }
}
