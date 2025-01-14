package com.observer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.observer.discord.DiscordWebhookService;
import com.observer.filter.Filter;
import com.observer.filter.FilterDividendPayoutRatio;
import com.observer.filter.FilterTradingVolume;
import com.observer.jdbc.DBService;
import com.observer.stock.StockDto;

public class Main {
    final static Scanner scanner = new Scanner(System.in);
    static String[] parameterArray;
    static Filter selectedFilter;
    static List<StockDto> stockDtoList;

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            Filter filterTradingVolume = new FilterTradingVolume(new String[] { "40" });
            Filter filterDividendPayoutRatio = new FilterDividendPayoutRatio(new String[] { "25", "50", "6", "300" });

            batchJob(filterTradingVolume);
            batchJob(filterDividendPayoutRatio);
        };

        long initialDelay = calculateInitialDelay(16, 30);
        long period = TimeUnit.DAYS.toSeconds(1);

        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);

        initialDelay = calculateInitialDelay(10, 00);
        period = TimeUnit.DAYS.toSeconds(1);

        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);

        while (true) {

            System.out.println("Choose a filter you want.");
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
                    selectedFilter = new FilterDividendPayoutRatio();
                    parameterArray = askParameter();
                    selectedFilter.setParameterArray(parameterArray);
                    selectedFilter.setDescription(parameterArray);
                    break;
                case 2:
                    System.out.println("You chose Number 2.");
                    selectedFilter = new FilterTradingVolume();
                    parameterArray = askParameter();
                    selectedFilter.setParameterArray(parameterArray);
                    selectedFilter.setDescription(parameterArray);
                    break;
            }

            stockDtoList = DBService.filterStock(selectedFilter);
            DiscordWebhookService.sendDiscordWebhookMessage(selectedFilter, stockDtoList);
        }

    }

    private static String[] askParameter() {
        System.out.printf("A query for filtering stock is '%s'.", selectedFilter.getQuery());
        System.out.println("\n\n");
        System.out.println("Fill all '?' in order with a value as you want.");
        System.out.println("Please separate each value with '/'.");

        return scanner.next().split("/");
    }

    private static void batchJob(Filter filter) {
        stockDtoList = DBService.filterStock(filter);
        DiscordWebhookService.sendDiscordWebhookMessage(filter, stockDtoList);
    }

    private static long calculateInitialDelay(int targetHour, int targetMinute) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(targetHour).withMinute(targetMinute).withSecond(0).withNano(0);

        if (now.isAfter(nextRun)) {
            // If the current time is after the target time, schedule for the next day
            nextRun = nextRun.plusDays(1);
        }

        ZonedDateTime nowZoned = ZonedDateTime.of(now, ZoneId.systemDefault());
        ZonedDateTime nextRunZoned = ZonedDateTime.of(nextRun, ZoneId.systemDefault());

        return TimeUnit.MILLISECONDS
                .toSeconds(nextRunZoned.toInstant().toEpochMilli() - nowZoned.toInstant().toEpochMilli());
    }
}