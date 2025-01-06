package com.observer;

import java.util.List;

import com.observer.discordWebhookMessage.DiscordWebhookMessage;
import com.observer.jdbc.DBService;

public class Main {
    public static void main(String[] args) {

        List<StockDto> filterDY = DBService.filterStock();

        String title = "FilterDY";
        String description = "시가총액 상위 300개 종목 중 배당수익률(DY) 8% 이상인 종목";
        DiscordWebhookMessage message = DiscordWebhookService.createDiscordWebhookMessage(title, description, filterDY);
        DiscordWebhookService.sendDiscordWebhookMessage(message);
    }
    
}