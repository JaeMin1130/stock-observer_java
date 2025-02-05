package com.observer.jdbc;

import static com.observer.jdbc.DBConnector.connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.observer.filter.Filter;
import com.observer.stock.StockDto;

public class DBService {

    private DBService() {
    };

    public static List<StockDto> filterStock(Filter filter) {
        System.out.printf("\n%s starts at %s.\n", filter.getTitle(), LocalTime.now());

        final List<StockDto> stockDtoList = new ArrayList<>();
        try (final Connection conn = connect();
                final PreparedStatement ps = conn.prepareStatement(filter.getQuery());
                final ResultSet rs = DBUtil.readDataPrepared(conn, ps, filter.getParameterArray())) {
            while (rs.next()) {

                int rsColumnCount = rs.getMetaData().getColumnCount();
                Map<String, Double> indicatorMap = new LinkedHashMap<>();
                for (int i = 1; i <= rsColumnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    if (columnName.startsWith("INDICATOR_")) {
                        indicatorMap.put(columnName.substring(columnName.indexOf("_") + 1, columnName.length()),
                                rs.getDouble(columnName));
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
