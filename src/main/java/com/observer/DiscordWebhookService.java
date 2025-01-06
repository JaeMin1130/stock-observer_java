package com.observer;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.observer.discordWebhookMessage.DiscordWebhookMessage;
import com.observer.discordWebhookMessage.Embed;
import com.observer.discordWebhookMessage.Field;

public class DiscordWebhookService {
    public static void sendDiscordWebhookMessage(DiscordWebhookMessage message) {
        final Properties properties = new Properties();
        String webhookUrl = "";

        try (final FileInputStream fis = new FileInputStream("src/main/resources/discord.properties")) {
            properties.load(fis);
            webhookUrl = properties.getProperty("discord.url");

        } catch (IOException e) {
            System.err.println("Error reading configuration file: " + e.getMessage());
        }

        // Create an HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(message.toString()))
                .build();

        // Send the request
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static DiscordWebhookMessage createDiscordWebhookMessage(String title, String description, List<StockDto> stockDtoList){
       List<Field> fields = new ArrayList<>();
        for(StockDto stockDto : stockDtoList){
            fields.add(stockDto.toField());
        }

        List<Embed> embeds = new ArrayList<>();
        Embed embed1 = new Embed(title, description, fields);
        embeds.add(embed1);
        
        DiscordWebhookMessage message = new DiscordWebhookMessage(embeds);
        
        return message;
    }
}
