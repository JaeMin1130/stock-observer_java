package com.observer.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.observer.StockDto;

public class DBUtil {
    public static List<StockDto> readData(Connection conn, String query) {
        List<StockDto> dataList = new ArrayList<>();
        System.out.printf("JDBC starts at %s \n", LocalTime.now());
        try (final Statement stmt = conn.createStatement();
                final ResultSet rs = stmt.executeQuery(query);) {

            System.out.printf("JDBC ends at %s \n", LocalTime.now());
            int rsColumnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                switch (rsColumnCount) {
                    case 6:
                        dataList.add(new StockDto.Builder()
                                .companyName(rs.getString("COMPANYNAME"))
                                .close(rs.getString("CLOSE"))
                                .sma(rs.getString("SMA"))
                                .closeSmaRatio(rs.getString("CLOSESMARATIO"))
                                .capRank(rs.getString("CAPRANK"))
                                .date(rs.getString("DATE"))
                                .build());
                        break;

                    default:
                        dataList.add(new StockDto.Builder()
                                .companyName(rs.getString("COMPANYNAME"))
                                .close(rs.getString("CLOSE"))
                                .sma(rs.getString("SMA"))
                                .closeSmaRatio(rs.getString("CLOSESMARATIO"))
                                .dy(rs.getString("DY"))
                                .capRank(rs.getString("CAPRANK"))
                                .date(rs.getString("DATE"))
                                .build());
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataList;
    }
}
