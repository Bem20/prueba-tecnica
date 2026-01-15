package app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public final class DbConnection {

    private DbConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                app.config.DbConfig.URL,
                app.config.DbConfig.USER,
                app.config.DbConfig.PASSWORD
        );
    }
}