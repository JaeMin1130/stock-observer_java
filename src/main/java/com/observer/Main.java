package com.observer;

import static com.observer.util.FilePath.QUERY;

import java.util.List;
import java.util.Scanner;

import com.observer.discord.DiscordWebhookService;
import com.observer.jdbc.DBService;
import com.observer.stock.StockDto;
import com.observer.util.FileReader;

public class Main {
    static String query;
    static String[] parameterArray;
    final static Scanner scanner = new Scanner(System.in);
    static String title;
    static String description;
    static List<StockDto> stockDtoList;

    public static void main(String[] args) {

        while (true) {

            System.out.println("Choose parameters you want to use as a filter");
            System.out.println("0: Exit, 1: DividendPayoutRatio and MarketCap, 2: TradingVolume and SMA20");
            final int filterNo = scanner.nextInt();

            switch (filterNo) {
                case 0:
                    System.out.println("You chose Number 0.");
                    System.out.println("The program is terminated.");
                    scanner.close();
                    System.exit(0);
                case 1:
                    System.out.println("You chose Number 1.");
                    askParameter("query.dividendPayoutRatio");

                    title = "FilterDividendPayoutRatio";
                    description = String.format(
                            "Stocks which dividendPayoutRatio is between %s%% ~ %s%% and dy is %s%% over in top%s marketcap",
                            parameterArray);
                    break;
                case 2:
                    System.out.println("You chose Number 2.");
                    askParameter("query.tradingVolume");

                    title = "FilterTradingVolume";
                    description = String.format("Stocks which close increses %s%% over sma20 in top100 tradingvolume",
                            parameterArray);

                    break;
            }
            stockDtoList = DBService.filterStock(query, parameterArray);
            DiscordWebhookService.sendDiscordWebhookMessage(title, description, stockDtoList);
        }
    }

    private static void askParameter(String queryName) {
        query = FileReader.read(QUERY).getProperty(queryName);
        System.out.printf("A query for filtering stock is '%s'.", query);
        System.out.println("\n\n");
        System.out.println("Fill all '?' with a value as you want.");
        System.out.println("Please separate each value with a '/'.");
        parameterArray = scanner.next().split("/");
    }
}