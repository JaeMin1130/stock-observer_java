package com.observer.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.observer.stock.StockDto;
import com.observer.util.FileReader;

import static com.observer.jdbc.DBConnector.connect;
import static com.observer.util.FilePath.QUERY;

public class DBService {
    public static List<StockDto> filterStock(String queryName, int param) {
        System.out.printf("\nFiltering starts at %s.\n", LocalTime.now());

        final List<StockDto> stockDtoList = new ArrayList<>();
        final String query = FileReader.read(QUERY).getProperty(queryName);
        try (final Connection conn = connect();
                final PreparedStatement ps = conn.prepareStatement(query);
                final ResultSet rs = DBUtil.readDataPrepared(conn, ps, param);) {

            int rsColumnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                switch (rsColumnCount) {
                    case 7:
                        stockDtoList.add(new StockDto.Builder()
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
                        stockDtoList.add(new StockDto.Builder()
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.printf("Filtering ends at %s.\n", LocalTime.now());
        System.out.printf("\nA number of stocks filtered is %d.\n", stockDtoList.size());
        return stockDtoList;
    }
}
