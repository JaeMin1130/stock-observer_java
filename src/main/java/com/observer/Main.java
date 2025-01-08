package com.observer;

import java.util.List;
import java.util.Scanner;

import com.observer.discord.DiscordWebhookService;
import com.observer.jdbc.DBService;
import com.observer.stock.StockDto;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        int param;
        String title;
        String description;
        List<StockDto> stockDtoList;

        while (true) {

            System.out.println("Choose parameters you want to use as a filter");
            System.out.println("1: DY and MarketCap, 2: TradingValue and SMA20, 3: Exit");
            final int filterNo = scanner.nextInt();

            switch (filterNo) {
                case 1:
                    System.out.println("You chose Number 1.");
                    System.out.println("Set a figure of DY(%).");
                    param = scanner.nextInt();

                    title = "FilterDY";
                    description = String.format("Stocks which dy is %s%% over in top300 marketcap", param);
                    stockDtoList = DBService.filterStock("query.dy", param);
                    DiscordWebhookService.sendDiscordWebhookMessage(title, description, stockDtoList);

                    continue;
                case 2:
                    System.out.println("You chose Number 2.");
                    System.out.println("Set a ratio of close over sma20(%).");
                    param = scanner.nextInt();

                    title = "FilterTradingValue";
                    description = String.format("Stocks which close increses %s%% over sma20 in top100 tradingvalue", param);
                    stockDtoList = DBService.filterStock("query.tradingValue", param);
                    DiscordWebhookService.sendDiscordWebhookMessage(title, description, stockDtoList);

                    continue;
                case 3:
                    System.out.println("You chose Number 3.");
                    System.out.println("The program is terminated.");
                    scanner.close();
                    break;
            }
            break;
        }
    }
}