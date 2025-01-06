package com.observer.discordWebhookMessage;

import java.util.List;

public class Embed {
    private String title;
    private String description;
    private final String color = "15258703";
    private List<Field> fields;

    public Embed(String title, String description, List<Field> fields) {
      this.title = title;
      this.description = description;
      this.fields = fields;
    }

    public String toString(){
      StringBuilder strBuilder = new StringBuilder();
      strBuilder.append(String.format("{\"title\":\"%s\", \"description\":\"%s\", \"color\":%s, \"fields\":[", title, description, color));
      for(Field field : fields){
        strBuilder.append(field.toString());
      }
      strBuilder.replace(strBuilder.length()-1, strBuilder.length(), "]},");
      return strBuilder.toString();
    }
}
