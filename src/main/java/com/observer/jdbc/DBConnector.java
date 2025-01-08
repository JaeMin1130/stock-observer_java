package com.observer.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.observer.util.FileReader;
import static com.observer.util.FilePath.JDBC;
public class DBConnector {
    private static final Properties properties = FileReader.read(JDBC);
    private static final String jdbcUrl = properties.getProperty("db.url");
    private static final String userName = properties.getProperty("db.username");
    private static final String password = properties.getProperty("db.password");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, userName, password);
    }
}
