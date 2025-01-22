package com.observer.discord;

import static com.observer.file.FilePath.DISCORD;

import java.util.List;

import com.observer.file.FileReader;

public class Embed {
  private String title;
  private String description;
  private final String color = FileReader.read(DISCORD).getProperty("discord.color");
  private List<Field> fields;

  public Embed(String title, String description, List<Field> fields) {
    this.title = title;
    this.description = description;
    this.fields = fields;
  }

  public List<Field> getFields() {
    return this.fields;
  }

  public String toJson() {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(String.format("{\"title\":\"%s\", \"description\":\"%s\", \"color\":%s, \"fields\":[", title,
        description, color));

    if (!fields.isEmpty()) {
      for (Field field : fields) {
        strBuilder.append(field.toJson());
      }
      strBuilder.replace(strBuilder.length() - 1, strBuilder.length(), "]},");
    } else {
      strBuilder.append("{\"name\": \"Empty\", \"value\": \"There are no stocks that match the filter for today.\"}]},");
    }

    return strBuilder.toString();
  }
}
