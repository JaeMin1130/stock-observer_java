package com.observer.discord;

import java.util.List;

import com.observer.util.FileReader;
import static com.observer.util.FilePath.DISCORD;

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

  public String toJson() {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(String.format("{\"title\":\"%s\", \"description\":\"%s\", \"color\":%s, \"fields\":[", title,
        description, color));
    for (Field field : fields) {
      strBuilder.append(field.toJson());
    }
    strBuilder.replace(strBuilder.length() - 1, strBuilder.length(), "]},");
    return strBuilder.toString();
  }
}
