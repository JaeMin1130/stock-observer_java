package com.observer.jdbc;

import static com.observer.jdbc.DBConnector.connect;
import static com.observer.util.FilePath.QUERY;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.observer.stock.StockDto;
import com.observer.util.FileReader;

public class DBService {
    
    private DBService() {};

    public static List<StockDto> filterStock(String queryName, int param) {
        System.out.printf("\nFiltering starts at %s.\n", LocalTime.now());

        final List<StockDto> stockDtoList = new ArrayList<>();
        final String query = FileReader.read(QUERY).getProperty(queryName);
        try (final Connection conn = connect();
                final PreparedStatement ps = conn.prepareStatement(query);
                final ResultSet rs = DBUtil.readDataPrepared(conn, ps, param);) {
            while (rs.next()) {

                int rsColumnCount = rs.getMetaData().getColumnCount();
                Map<String, Double> indicatorMap = new HashMap<>();
                for (int i = 1; i <= rsColumnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    if (columnName.startsWith("INDICATOR_")) {
                        indicatorMap.put(columnName.substring(columnName.indexOf("_") + 1, columnName.length()),
                                rs.getDouble(i));
                        continue;
                    }
                }

                stockDtoList.add(new StockDto.Builder()
                        .companyName(rs.getString("COMPANYNAME"))
                        .close(rs.getInt("CLOSE"))
                        .capRank(rs.getInt("CAPRANK"))
                        .tradingVolume(rs.getInt("TRADINGVOLUME"))
                        .date(rs.getDate("DATE").toLocalDate())
                        .indicatorMap(indicatorMap)
                        .build());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.printf("Filtering ends at %s.\n", LocalTime.now());
        System.out.printf("\nA number of stocks filtered is %d.\n", stockDtoList.size());
        return stockDtoList;
    }
}
