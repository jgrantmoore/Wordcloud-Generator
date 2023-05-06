package cs1302.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import cs1302.api.WebScraperResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.scene.image.Image;

/**
 * The class that creates a wordcloud out of a given WebScraperResult object.
 */
public class WordCloudApi {

    /** Represents the result of a call to the word cloud api. */
    private static class WordCloudResponse {
        String imgUrl;
    } //WordCloudResponse


    /** Http Client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private static final String ENDPOINT = "https://quickchart.io/wordcloud";

    /**
     * Creates an image from the word cloud.
     * @param result a WebScraperResult to pass to the getImgLocation Method
     * @return Image returns the wordcloud image
     */
    public static Image call(WebScraperResult result) {
        System.out.println("Creating the Word Cloud");
        try {
            Image returnImg = new Image(getImgLocation(result));

            return returnImg;
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        } //catch

    } //call


    /**
     * The method that actually calls the api.
     * @param result The words to call with the api
     * @return the url of the wordcloud image
     */
    private static String getImgLocation(
        WebScraperResult result) throws IOException, InterruptedException {

        String[] words = result.data.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Math.min(200, words.length); i++) {
            builder.append(words[i]).append(" ");
        }
        String trimmedResults = builder.toString().trim();

        String url = String.format(
            "%s?text=%s&format=png&width=700&height=700",
            WordCloudApi.ENDPOINT,
            URLEncoder.encode(trimmedResults, StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
        HttpResponse<String> response = HTTP_CLIENT
            .send(request, BodyHandlers.ofString());
        final int statusCode = response.statusCode();
        System.out.println(response);
        if (statusCode != 200) {
            throw new IOException("response status code not 200: " + statusCode);
        }

        return url;

    } //getImageView


} //WordCloudApi
