package io.github.ottocline.stocksentinal;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
  private static final Dotenv dotenv = Dotenv.load();

  public static String getApiKey() {
    return dotenv.get("API_KEY");
  }
}