package com.observer.discord;

import java.util.List;
import java.util.Properties;

import com.observer.util.FileReader;

import static com.observer.util.FilePath.DISCORD;

public class DiscordWebhookMessage {
  private final Properties properties = FileReader.read(DISCORD);
  private final String username = properties.getProperty("discord.username");
  private final String avatar_url = properties.getProperty("discord.avatar_url");
  private List<Embed> embeds;

  public DiscordWebhookMessage(List<Embed> embeds) {
    this.embeds = embeds;
  }

  public DiscordWebhookMessage getEmptyMessage() {
    embeds.add(new Embed("Empty", "Empty", null));
    return new DiscordWebhookMessage(embeds);
  }

  public String toJson() {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder
        .append(String.format("{\"username\":\"%s\", \"avatar_url\":\"%s\", \"embeds\": [", username, avatar_url));

    for (Embed embed : embeds) {
      strBuilder.append(embed.toJson());
    }
    
    strBuilder.replace(strBuilder.length() - 1, strBuilder.length(), "]}");
    return strBuilder.toString();
  }
}
