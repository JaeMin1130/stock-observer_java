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

        Filter filterDividend = new FilterDividend();
        Filter filterHugeDrop = new FilterHugeDrop();
        Filter filterTemp = new FilterTemp();
        Map<Filter, List<StockDto>> resultMap = new LinkedHashMap<>();
        
        Runnable task = () -> {
            System.out.printf("A scheduled task starts at %s\n", LocalDateTime.now());
            DBService.upsertIndicator();
            
            resultMap.put(filterDividend, DBService.filterStock(filterDividend));
            resultMap.put(filterHugeDrop, DBService.filterStock(filterHugeDrop));
            resultMap.put(filterTemp, DBService.filterStock(filterTemp));
            
            DiscordWebhookService.sendDiscordWebhookMessage(resultMap);
            System.out.printf("A scheduled task ends at %s\n", LocalDateTime.now());
        };

        try{
            System.out.println("Test sending starts\n");
            scheduler.schedule(task, 0, TimeUnit.SECONDS);
            System.out.println("Test sending finished\n");
    
            long initialDelay = calculateInitialDelay(10, 0);
            long period = TimeUnit.DAYS.toSeconds(1);
            scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
    
            initialDelay = calculateInitialDelay(16, 30);
            scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
        }catch(Exception e){
            e.printStackTrace();
        }
        
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