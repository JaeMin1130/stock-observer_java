package com.observer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
            System.out.println("Task starts");
            DBService.upsertIndicator();

            Filter filterDividend = new FilterDividend(new String[] { "25", "75", "5", "-3", "300" });
            Filter filterHugeDrop = new FilterHugeDrop(new String[] { "-10", "300" });
            Filter filterTemp = new FilterTemp(new String[] { "-8", "2", "3", "500", "2000" });

            Map<Filter, List<StockDto>> resultMap = new LinkedHashMap<>();
            resultMap.put(filterDividend, DBService.filterStock(filterDividend));
            resultMap.put(filterHugeDrop, DBService.filterStock(filterHugeDrop));
            resultMap.put(filterTemp, DBService.filterStock(filterTemp));

            DiscordWebhookService.sendDiscordWebhookMessage(resultMap);
        };

        long initialDelay = calculateInitialDelay(10, 0);
        long period = TimeUnit.DAYS.toSeconds(1);
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);

        initialDelay = calculateInitialDelay(15, 31);
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
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