package com.observer.discordWebhookMessage;

public class Field {
    private String name;
    private String value;
    private final String inline = "true";

    public Field(String name, String value) {
      this.name = name;
      this.value = value;
    }

    public String toString() {
      return String.format("{\"name\":\"%s\", \"value\":\"%s\", \"inline\":%s},", name, value, inline);
    }
}
