package com.observer.discord;

import static com.observer.file.FilePath.DISCORD;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.observer.file.FileReader;
import com.observer.filter.Filter;
import com.observer.stock.StockDto;

public class DiscordWebhookService {
    private DiscordWebhookService() {
    };

    public static void sendDiscordWebhookMessage(Map<Filter, List<StockDto>> resultMap) {

        final HttpClient client = HttpClient.newHttpClient();
        final String webhookUrl = FileReader.read(DISCORD).getProperty("discord.url");
        final DiscordWebhookMessage message = createDiscordWebhookMessage(resultMap);

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

    public static void sendTestMessage() {

        final HttpClient client = HttpClient.newHttpClient();
        final String webhookUrl = FileReader.read(DISCORD).getProperty("discord.url");

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("{\"username\": \"Webhook\",\"avatar_url\": \"https://i.imgur.com/4M34hi2.png\",\"embeds\": [{\"title\": \"Test Message\",\"description\": \"It is a sample message for a test.\"}]}"
))
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(
                    response.statusCode() == 204 ? "\nSucceeded sending a Message" : "\nFailed sending a message");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DiscordWebhookMessage createDiscordWebhookMessage(Map<Filter, List<StockDto>> resultMap) {
        List<Embed> embeds = new ArrayList<>();
        
        for(Entry<Filter, List<StockDto>> entry : resultMap.entrySet()){
            Filter filter = entry.getKey();
            List<StockDto> stockDtoList = entry.getValue();   
            List<Field> fields = new ArrayList<>();

            for (StockDto stockDto : stockDtoList) {
                // 오늘 날짜가 아니면 스킵
                if(!stockDto.getDate().equals(LocalDate.now())) continue;
                fields.add(stockDto.toField());
            }

            embeds.add(new Embed(filter.getTitle(), filter.getDescription(), fields));
        }

        return new DiscordWebhookMessage(embeds);
    }
}
