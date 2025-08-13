package io.github.ottocline.stocksentinal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;

import java.util.Properties;


public class App {
  public static void main(String[] args) {
    try {
      String apiKey = Config.getApiKey();
      String endpoint = "https://newsapi.org/v2/everything?q=stocks&language=en&pageSize=10&apiKey=" + apiKey;

      URL apiURL = new URL(endpoint);
      HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
      connection.setRequestMethod("GET");

      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuilder response = new StringBuilder();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }

      in.close();

      String articles = NewsParser.parseNews(response.toString());

      Properties props = new Properties();
      props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,sentiment");
      StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

      String sentimentResult = getSentiment(articles, pipeline);
      String averageSent = makeSentimentString(averageSentimentNums(sentimentResult));
      System.out.println(sentimentResult + "\n" + "Market sentiment is looking " + averageSent + " at the moment.");

    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  public static String getSentiment(String text, StanfordCoreNLP pipeline) {
    // create an Annotation object with the input text
    Annotation annotation = new Annotation(text);

    // annotate the text using the pipeline
    pipeline.annotate(annotation);

    // get sentences from the annotated text
    var sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

    // for each sentence get its sentiment
    StringBuilder result = new StringBuilder();
    for (var sentence : sentences) {
      String sentenceText = sentence.get(CoreAnnotations.TextAnnotation.class);
      Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
      int sentiment = RNNCoreAnnotations.getPredictedClass(tree);

      //call sentimentboost to boost based on market indicators
      sentiment = SentimentBoost.boostSentiment(sentenceText, sentiment);

      result.append(sentenceText)
              .append(" --> Sentiment: ")
              .append(makeSentimentString(sentiment) + " --> ")
              .append("Sentiment score: ")
              .append(sentiment + " ")
              .append("\n");
    }

    String summary = result.toString();


    return summary;
  }

  public static String makeSentimentString(int sentimentNum) {
    switch (sentimentNum) {
      case -1: return "cooked";
      case 0: return "very negative";
      case 1: return "slight negative";
      case 2: return "neutral";
      case 3: return "slight positive";
      case 4: return "very positive";
      case 5: return "goated";
      default: return "unknown";
    }
  }

  public static int averageSentimentNums(String summary) {
    String[] split = summary.split(" ");
    int sum = 0;
    int count = 0;
    for (int i = 1; i < split.length; i++) {
      if (split[i - 1].equals("Sentiment") && split[i].equals("score:")) {
        sum = sum + Integer.parseInt(split[i + 1]);
        count++;
      }
    }
    return sum / count;
  }


}
