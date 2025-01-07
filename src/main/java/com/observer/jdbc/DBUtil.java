package com.observer.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.observer.stock.StockDto;

public class DBUtil {
    public static List<StockDto> readDataPrepared(Connection conn, String query,
            int param) {
        List<StockDto> dataList = new ArrayList<>();
        try (final PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, param);
            final ResultSet rs = ps.executeQuery();

            int rsColumnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                switch (rsColumnCount) {
                    case 7:
                        dataList.add(new StockDto.Builder()
                                .companyName(rs.getString("COMPANYNAME"))
                                .close(rs.getInt("CLOSE"))
                                .sma(rs.getDouble("SMA"))
                                .closeSmaRatio(rs.getDouble("CLOSESMARATIO"))
                                .capRank(rs.getInt("CAPRANK"))
                                .tradingVolume(rs.getInt("TRADINGVOLUME"))
                                .date(rs.getDate("DATE").toLocalDate())
                                .build());
                        break;

                    default:
                        dataList.add(new StockDto.Builder()
                                .companyName(rs.getString("COMPANYNAME"))
                                .close(rs.getInt("CLOSE"))
                                .sma(rs.getDouble("SMA"))
                                .closeSmaRatio(rs.getDouble("CLOSESMARATIO"))
                                .dy(rs.getDouble("DY"))
                                .capRank(rs.getInt("CAPRANK"))
                                .tradingVolume(rs.getInt("TRADINGVOLUME"))
                                .date(rs.getDate("DATE").toLocalDate())
                                .build());
                        break;
                }
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataList;
    }
}