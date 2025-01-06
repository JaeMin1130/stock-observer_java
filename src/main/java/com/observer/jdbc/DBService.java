package com.observer.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.observer.StockDto;

public class DBService {
    public static List<StockDto> filterStock() {

        List<StockDto> filterDy = new ArrayList<>();
        try (final Connection conn = DBConnector.connect()) {
            // 거래대금(TradingValue) 상위 100개(50억 원 이상) 종목 중 20일 이동평균선(SMA) 대비 40% 이상 상승한 종목
            String query = "SELECT a.companyname, close, sma, round((CLOSE / sma -1) * 100, 1) closeSmaRatio, capRank, date FROM (SELECT companyname, close, rank() over(ORDER BY marketcap desc) capRank, date FROM stock WHERE date = current_date AND tradingvalue >= 5000000000 ORDER BY tradingvalue  DESC LIMIT 100) a NATURAL JOIN (SELECT companyname, round(avg(CLOSE), 2) AS sma FROM stock WHERE date IN (SELECT distinct(date) FROM stock ORDER BY date DESC LIMIT 20) GROUP BY companyname) b WHERE CLOSE / sma - 1 >= 0.4 ORDER BY closeSmaRatio DESC";
            // strategyTradingValue = readData(conn, query);

            // 시가총액(MarketCap) 상위 300개 종목 중 배당수익률(DY) 5% 이상인 종목
            query = "SELECT a.companyname, close, sma, round((CLOSE / sma -1) * 100, 1) closeSmaRatio, round(CAST(dps AS decimal) / close * 100, 2) dy, capRank, date FROM (SELECT companyname, close, dps, rank() over(ORDER BY marketcap desc) capRank, date FROM stock WHERE date = current_date ORDER BY marketcap DESC LIMIT 300) a NATURAL JOIN (SELECT companyname, round(avg(CLOSE), 2) AS sma FROM stock WHERE date IN (SELECT distinct(date) FROM stock ORDER BY date DESC LIMIT 20) GROUP BY companyname) b WHERE 1=1 AND CAST(dps AS decimal) / close * 100 >= 8 ORDER BY dy DESC";
            filterDy = DBUtil.readData(conn, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filterDy;
    }
}
