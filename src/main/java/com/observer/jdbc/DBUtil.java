package com.observer.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    
    private DBUtil(){}

    public static ResultSet readDataPrepared(Connection conn, PreparedStatement ps, int param) {

        try {
            ps.setInt(1, param);

            final ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}