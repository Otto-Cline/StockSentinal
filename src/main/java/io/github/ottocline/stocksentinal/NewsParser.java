package io.github.ottocline.stocksentinal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NewsParser {
  public static String parseNews(String jsonResponse) {
    JsonObject json = JsonParser.parseString(jsonResponse).getAsJsonObject();
    JsonArray articles = json.getAsJsonArray("articles");

    StringBuilder result = new StringBuilder();

    for (int i = 0; i < articles.size(); i++) {
      JsonObject article = articles.get(i).getAsJsonObject();
      String title = article.get("title").getAsString();
      String url = article.get("url").getAsString();
      String publishedAt = article.get("publishedAt").getAsString();
      result.append(title).append(". ");
    }
    return result.toString();
  }
}
