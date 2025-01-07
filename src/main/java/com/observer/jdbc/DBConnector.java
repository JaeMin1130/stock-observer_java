package com.observer.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.observer.util.FileReader;

public class DBConnector {
    private static final String PATH = "src/main/resources/db.properties";
    private static final Properties properties = FileReader.read(PATH);
    private static final String jdbcUrl = properties.getProperty("db.url");
    private static final String userName = properties.getProperty("db.username");
    private static final String password = properties.getProperty("db.password");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, userName, password);
    }
}
