package com.observer.discordWebhookMessage;

import java.util.List;

public class DiscordWebhookMessage {
  private final String username = "Webhook";
  private final String avatar_url = "https://i.imgur.com/4M34hi2.png";
  private List<Embed> embeds;

  public DiscordWebhookMessage(List<Embed> embeds) {
    this.embeds = embeds;
  }

  public String toString(){
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(String.format("{\"username\":\"%s\", \"avatar_url\":\"%s\", \"embeds\": [", username, avatar_url));
    for(Embed embed : embeds){
      strBuilder.append(embed.toString());
    }
    strBuilder.replace(strBuilder.length()-1, strBuilder.length(), "]}");
    return strBuilder.toString();
  }
}
