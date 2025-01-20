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
import com.observer.filter.FilterDividend;
import com.observer.filter.FilterHugeDrop;
import com.observer.filter.FilterTemp;
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
            DBService.upsertIndicator();

            Filter filterDividend = new FilterDividend(new String[] { "25", "75", "5", "-3", "1", "300" });
            executeFilter(filterDividend);

            Filter filterHugeDrop = new FilterHugeDrop(new String[] { "-30", "-10", "100" });
            executeFilter(filterHugeDrop);

            Filter filterTemp = new FilterTemp(new String[] { "-8", "2", "3", "500", "2000" });
            executeFilter(filterTemp);
            // Filter filterTradingVolume = new FilterTradingVolume(new String[] { "40",
            // "100"});
            // executeFilter(filterTradingVolume);
        };

        long initialDelay = calculateInitialDelay(10, 0);
        long period = TimeUnit.DAYS.toSeconds(1);
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);

        initialDelay = calculateInitialDelay(17, 00);
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);

        while (true) {

            System.out.println("Choose a filter you want.");
            System.out.println("0: Exit, 1: Dividend, 2: Huge Drop, 3: Temp");
            final int filterNo = scanner.nextInt();

            switch (filterNo) {
                case 0:
                    System.out.println("You chose Number 0.");
                    System.out.println("The program is terminated.");
                    scanner.close();
                    System.exit(0);
                case 1:
                    System.out.println("You chose Number 1.");
                    selectedFilter = new FilterDividend();
                    parameterArray = askParameter();
                    selectedFilter.setParameterArray(parameterArray);
                    selectedFilter.setDescription(parameterArray);
                    break;
                case 2:
                    System.out.println("You chose Number 2.");
                    selectedFilter = new FilterHugeDrop();
                    parameterArray = askParameter();
                    selectedFilter.setParameterArray(parameterArray);
                    selectedFilter.setDescription(parameterArray);
                    break;
                case 3:
                    System.out.println("You chose Number 3.");
                    selectedFilter = new FilterTemp();
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

    private static void executeFilter(Filter filter) {
        System.out.println("Start Filtering");
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