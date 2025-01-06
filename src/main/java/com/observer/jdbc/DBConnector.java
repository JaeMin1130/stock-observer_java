package com.observer.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {
    private static final String filePath = "src/main/resources/jdbc.properties";
    private static String jdbcUrl;
    private static String userName;
    private static String password;

    public static Connection connect() throws SQLException {
        try (final FileInputStream file = new FileInputStream(filePath);) {
            Properties properties = new Properties();
            properties.load(file);
            jdbcUrl = properties.getProperty("db.url");
            userName = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("Error reading configuration file: " + e.getMessage());
        }
        return DriverManager.getConnection(jdbcUrl, userName, password);
    }
}
