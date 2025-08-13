package io.github.ottocline.stocksentinal;

import java.util.Arrays;
import java.util.List;

public class SentimentBoost {
  private static final List<String> positiveKeywords = Arrays.asList("bullish", "rally", "surge",
          "record high", "gain", "profit", "growth", "uptrend", "upswing", "soar", "soars", "surges",
          "roar", "positive", "strong", "safe", "optimistic", "green", "buzz", "climb", "assure");
  private static final List<String> negativeKeywords = Arrays.asList("bearish", "downturn", "downtrend",
          "negative", "worries", "fear", "worry", "warns", "warn", "poor", "weak", "loss",
          "decline", "plummet", "risk", "plummets", "panic", "scare", "scares", "scared", "red",
          "liability");
  private static final List<String> veryPositiveKeywords = Arrays.asList("record high", "soar");
  private static final List<String> veryNegativeKeywords = Arrays.asList("record low", "plummet", "crash",
          "bubble", "2008", "disaster", "freefall");

  public static int boostSentiment(String text, int originalSentiment) {
    String lText = text.toLowerCase();

    for (String keyword : positiveKeywords) {
      if (lText.contains(keyword)) {
        return Math.min(originalSentiment + 2, 5);
      }
    }

    for (String keyword : negativeKeywords) {
      if (lText.contains(keyword)) {
        return Math.max(originalSentiment - 2, -1);
      }
    }

    for (String keyword : veryPositiveKeywords) {
      if (lText.contains(keyword)) {
        return Math.max(originalSentiment + 3, 5);
      }
    }

    for (String keyword : veryNegativeKeywords) {
      if (lText.contains(keyword)) {
        return Math.max(originalSentiment - 3, -1);
      }
    }

    return originalSentiment;
  }

}
