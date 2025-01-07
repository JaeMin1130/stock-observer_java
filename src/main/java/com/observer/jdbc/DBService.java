package com.observer.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.observer.stock.StockDto;
import com.observer.util.FileReader;

public class DBService {
    public static List<StockDto> filterStock(String queryName, int param) {

        List<StockDto> stockDtoList = new ArrayList<>();
        final String query = FileReader.read("src/main/resources/query.properties").getProperty(queryName);
        try (final Connection conn = DBConnector.connect()) {

            stockDtoList = DBUtil.readDataPrepared(conn, query, param);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockDtoList;
    }
}