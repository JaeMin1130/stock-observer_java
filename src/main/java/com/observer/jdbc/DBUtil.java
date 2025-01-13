package com.observer.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    
    private DBUtil(){}

    public static ResultSet readDataPrepared(Connection conn, PreparedStatement ps, String[] parameterArray) {

        try {
            final int parameterCount = ps.getParameterMetaData().getParameterCount();
            for(int i = 1; i <= parameterCount; i++){
                ps.setInt(i, Integer.parseInt(parameterArray[i-1]));
            }
            final ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}