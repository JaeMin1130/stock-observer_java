package com.observer;

import static com.observer.jdbc.DBConnector.connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.observer.filter.Filter;
import com.observer.filter.FilterWild;
import com.observer.jdbc.DBUtil;

public class BackTest {
    public static List<Parameter> getParameterList(int maxProfitCut, int maxLossCut, int day, Filter filter) {

        Map<Stock, List<Integer>> totalStockCloseMap = new LinkedHashMap<>();
        List<Parameter> parameterList = new ArrayList<>();

        try (final Connection conn = connect();
                final PreparedStatement ps = conn.prepareStatement(filter.getQuery());
                final Statement stmt = conn.createStatement();) {

            final ResultSet rs = DBUtil.readDataPrepared(conn, ps, filter.getParameterArray());

            List<Stock> stockList = new ArrayList<>();

            while (rs.next()) {
                stockList.add(new Stock(rs.getString("COMPANYNAME"), rs.getInt("CAPRANK"), rs.getString("DATE")));
            }

            for (Stock stock : stockList) {
                List<Integer> individualStockCloseList = new ArrayList<>();
                String query = String.format(
                        "SELECT close FROM stock WHERE date >= '%s' and date <= DATEADD('DAY', %d, DATE '%s') and companyname = '%s' order by date",
                        stock.date, day, stock.date, stock.companyName);
                final ResultSet stockCloseSet = stmt.executeQuery(query);
                while (stockCloseSet.next()) {
                    individualStockCloseList.add(stockCloseSet.getInt("CLOSE"));
                }

                if (individualStockCloseList.size() == 0)
                    continue;

                totalStockCloseMap.put(stock, individualStockCloseList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int profitCut = 1; profitCut <= maxProfitCut; profitCut++) {
            for (int lossCut = maxLossCut; lossCut < 0; lossCut++) {
                List<Result> resultList = new ArrayList<>();
                for (Entry<Stock, List<Integer>> eachStock : totalStockCloseMap.entrySet()) {

                    List<Integer> closeList = eachStock.getValue();
                    int curListSize = resultList.size();
                    int buyPrice = closeList.get(0);
                    int income = 0;

                    for (int curPrice : closeList) {
                        income = curPrice - buyPrice;

                        // 매도 규칙
                        if (((double) curPrice / buyPrice - 1) * 100 >= profitCut) {
                            resultList.add(new Result("Profit", eachStock.getKey(), buyPrice, curPrice, income));
                            break;
                        }
                        if (((double) curPrice / buyPrice - 1) * 100 <= lossCut) {
                            resultList.add(new Result("Loss", eachStock.getKey(), buyPrice, curPrice, income));
                            break;
                        }
                    }

                    // 지정 기간 동안 안 팔리면
                    if (resultList.size() == curListSize) {
                        resultList.add(new Result(income >= 0 ? "Profit" : "Loss", eachStock.getKey(), buyPrice,
                                closeList.get(closeList.size() - 1), income));
                    }
                }

                int totalIncome = resultList.stream().mapToInt(r -> r.income).sum();
                Parameter curParameter = new Parameter(profitCut, lossCut, totalIncome, resultList);
                parameterList.add(curParameter);
            }
        }
        return parameterList;
    }

    private static void printBestParameter(Parameter bestParameter) {

        if (bestParameter.resultList == null) {
            System.out.println("All parameters didn't return a profit");
            return;
        }

        Map<String, List<Result>> resultMap = new LinkedHashMap<>();
        for (Result result : bestParameter.resultList) {
            resultMap.computeIfAbsent(result.resultType, k -> new ArrayList<>()).add(result);
        }

        System.out.println("\n");
        System.out.println("Result Info divided by profit or loss");
        for (Entry<String, List<Result>> entry : resultMap.entrySet()) {
            System.out.println("\n");
            System.out.println(entry.getKey());
            for (Result result : entry.getValue()) {
                System.out.println(result);
            }
        }

        System.out.println();
        System.out.println("The best performed parameter is " + bestParameter);
        System.out.println();
        
        System.out.println("A total number of stocks: " + bestParameter.resultList.size());
        System.out.println("A number of stocks which return a Profit: " + resultMap.get("Profit").size());
        System.out.printf("Total Profit: %,d\n",
        bestParameter.resultList.stream().filter(r -> r.resultType.equals("Profit")).mapToInt(r -> r.income)
        .sum());
        System.out.println("A number of stocks which return a Loss: " + resultMap.get("Loss").size());
        System.out.printf("Total Loss: %,d\n",
        bestParameter.resultList.stream().filter(r -> r.resultType.equals("Loss")).mapToInt(r -> r.income)
        .sum());
        
        System.out.println();
        System.out.printf("Total Buying Price: %,d\n",
                bestParameter.resultList.stream().mapToInt(r -> r.buyPrice).sum());
        System.out.printf("Total Selling Price: %,d\n",
                bestParameter.resultList.stream().mapToInt(r -> r.sellPrice).sum());
        System.out.printf("Total Income: %,d\n", bestParameter.totalIncome);
        System.out.println();
    }

    public static void main(String[] args) {

        Filter filterWild = new FilterWild();
        List<Parameter> parameterList = getParameterList(15, -5, 10, filterWild);

        for (Parameter parameter : parameterList) {
            System.out.println(parameter);
        }

        var bestParameter = new Parameter(0, 0, 0, null);
        int maxTotalIncome = 0;
        for (Parameter curParameter : parameterList) {
            int curTotalIncome = curParameter.totalIncome;
            if (maxTotalIncome < curTotalIncome) {
                bestParameter = curParameter;
                maxTotalIncome = curTotalIncome;
            }
        }

        printBestParameter(bestParameter);
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
        return String.format("resultType: %s, stock: %s, buyPrice: %,d, sellPrice: %,d, income: %,d", resultType,
                stock.toString(), buyPrice,
                sellPrice, income);
    }
}

class Parameter {
    Integer profitCut;
    Integer lossCut;
    Integer totalIncome;
    List<Result> resultList;

    public Parameter(Integer profitCut, Integer lossCut, Integer totalIncome, List<Result> resultList) {
        this.profitCut = profitCut;
        this.lossCut = lossCut;
        this.totalIncome = totalIncome;
        this.resultList = resultList;
    }

    @Override
    public String toString() {
        return String.format("[profitCut: %d, lossCut: %d, total income: %,d]", profitCut, lossCut, totalIncome);
    }
}
