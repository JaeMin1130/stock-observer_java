package com.observer.discord;

import static com.observer.util.FilePath.DISCORD;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.observer.filter.Filter;
import com.observer.stock.StockDto;
import com.observer.util.FileReader;

public class DiscordWebhookService {
    private DiscordWebhookService() {
    };

    public static void sendDiscordWebhookMessage(Filter filter, List<StockDto> stockDtoList) {

        final HttpClient client = HttpClient.newHttpClient();
        final String webhookUrl = FileReader.read(DISCORD).getProperty("discord.url");
        final DiscordWebhookMessage message = DiscordWebhookService.createDiscordWebhookMessage(filter, stockDtoList);

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(message.toJson()))
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(
                    response.statusCode() == 204 ? "\nSucceeded sending a Message" : "\nFailed sending a message");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DiscordWebhookMessage createDiscordWebhookMessage(Filter filter,
            List<StockDto> stockDtoList) {

        List<Field> fields = new ArrayList<>();
        for (StockDto stockDto : stockDtoList) {
            // 오늘 날짜가 아니면 스킵
            if(stockDto.getDate() != LocalDate.now()) continue;
            fields.add(stockDto.toField());
        }

        List<Embed> embeds = new ArrayList<>();
        Embed embed1 = new Embed(filter.getTitle(), filter.getDescription(), fields);
        embeds.add(embed1);

        return new DiscordWebhookMessage(embeds);
    }
}
