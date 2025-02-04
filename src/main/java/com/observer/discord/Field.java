package com.observer.discord;

public class Field {
    private String name;
    private String value;
    private final String inline = "true";

    public Field(String name, String value) {
      this.name = name;
      this.value = value;
    }

    public String toJson() {
      return String.format("{\"name\":\"%s\", \"value\":\"%s\", \"inline\":%s},", name, value, inline);
    }
}
