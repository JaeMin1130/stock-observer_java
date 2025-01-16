package com.observer;

import static com.observer.jdbc.DBConnector.connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BackTest {

    public static void main(String[] args) {
        try (final Connection conn = connect();
                final Statement stmt = conn.createStatement();) {

            String query1 = "SELECT companyname, caprank, date FROM (SELECT a.companyname, CLOSE, round((CLOSE / sma3 -1) * 100, 1) as closesma3ratio, round((CLOSE / sma5 -1) * 100, 1) as closesma5ratio, round((CLOSE / sma12 -1) * 100, 1) as closesma12ratio, round((CLOSE / sma20 -1) * 100, 1) as closesma20ratio, capRank, tradingvolume, a.date FROM (SELECT *, rank() over(PARTITION BY date ORDER BY marketcap desc) capRank FROM stock) a left outer JOIN (SELECT * from INDICATOR) b ON a.companyname = b.companyname AND a.date = b.date) ";
            String query2 = "WHERE 1=1 AND closesma3ratio BETWEEN 0.5 AND 1 AND closesma12ratio < -10 and date <= current_date - 5 and tradingvolume < 500000 ORDER BY date";
            String query = query1 + query2;
            final ResultSet rs = stmt.executeQuery(query);

            List<Stock> stockList = new ArrayList<>();

            while (rs.next()) {
                stockList.add(new Stock(rs.getString("COMPANYNAME"), rs.getInt("CAPRANK"), rs.getString("DATE")));
            }

            List<Result> resultList = new ArrayList<>();
            for (Stock stock : stockList) {
                int curListSize = resultList.size();
                query = String.format(
                        "SELECT close FROM stock WHERE date between DATEADD('DAY', 1, DATE '%s') and DATEADD('DAY', 7, DATE '%s') and companyname = '%s' order by date",
                        stock.date, stock.date, stock.companyName);
                final ResultSet closeFor5Days = stmt.executeQuery(query);

                closeFor5Days.next();
                int buyPrice = closeFor5Days.getInt("CLOSE");
                int curPrice = 0;
                int income = 0;
                while (closeFor5Days.next()) {
                    curPrice = closeFor5Days.getInt("CLOSE");
                    income = curPrice - buyPrice;

                    // 매도 규칙
                    if (((double) curPrice / buyPrice - 1) * 100 >= 3
                            || ((double) curPrice / buyPrice - 1) * 100 <= -2) {

                        resultList.add(new Result(((double) curPrice / buyPrice - 1) * 100 >= 3 ? "Profit" : "Loss",
                                stock, buyPrice, curPrice, income));
                        break;
                    }
                }

                // 5일 동안 보유하면
                if (resultList.size() == curListSize) {
                    resultList.add(new Result("Loss", stock, buyPrice, curPrice, income));
                }

                System.out.printf("Progress: %d / %d\n", curListSize + 1, stockList.size());
            }

            System.out.println("\n\nCalculating a total income...");

            int[] totalResultTypeCount = new int[2];
            int totalBuyPrice = 0;
            int totalSellPrice = 0;
            int totalIncome = 0;

            for (Result result : resultList) {
                totalResultTypeCount[result.resultType.equals("Profit") ? 0 : 1]++;
                totalBuyPrice += result.buyPrice;
                totalSellPrice += result.sellPrice;
                totalIncome += result.income;
            }

            System.out.println("A total number of stocks: " + resultList.size());
            System.out.println("A number of stocks which return a Profit: " + totalResultTypeCount[0]);
            System.out.println("A number of stocks which return a Loss: " + totalResultTypeCount[1]);
            System.out.println("Total Buying Price: " + totalBuyPrice);
            System.out.println("Total Selling Price: " + totalSellPrice);
            System.out.println("Total Income: " + totalIncome);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class Stock {
    String companyName;
    Integer capRank;
    String date;

    Stock(String companyName, Integer capRank, String date) {
        this.companyName = companyName;
        this.capRank = capRank;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("[companyName: %s, capRank: %,d, date: %s]", companyName, capRank, date);
    }
}

class Result {
    String resultType;
    Stock stock;
    Integer buyPrice;
    Integer sellPrice;
    Integer income;

    Result(String resultType, Stock stock, Integer buyPrice, Integer sellPrice, Integer income) {
        this.resultType = resultType;
        this.stock = stock;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.income = income;
    }

    @Override
    public String toString() {
        return String.format("\nresultType: %s, stock: %s, buyPrice: %,d, sellPrice: %,d, income: %,d\n", resultType,
                stock.toString(), buyPrice,
                sellPrice, income);
    }
}