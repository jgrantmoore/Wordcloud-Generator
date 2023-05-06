package cs1302.api;

import java.net.MalformedURLException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import cs1302.api.WebScraperResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * The class that scrapes the specified website for the visible text.
 */
public class WebScraperApi {

    /** HTTP Client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    public static final String ENDPOINT = "https://api.api-ninjas.com/v1/webscraper";

    /**
     * Scrapes a website for the visible text.
     * @param incUrl the website that is going to me scraped.
     * @return WebScraperResult the result of the scraping
     */
    public static WebScraperResult scrape(String incUrl) {
        System.out.println("Attempting to scrape " + incUrl);
        System.out.println("This may take a couple seconds...");
        try {
            String url = String.format(
                "%s?url=%s&text_only=true",
                WebScraperApi.ENDPOINT,
                incUrl);
            WebScraperResult result = GSON.fromJson(call(url), WebScraperResult.class);

            return result;
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        } //catch
    } //scrape

    /**
     * A private class that actually calls the uri after it has been built.
     * @param uri the pre-built uri
     * @return String the visible text
     */
    private static String call(String uri) throws IOException, InterruptedException {
        System.out.println("Calling: " + uri);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .header("X-Api-Key", "RnkFTljJlpkmThufMGMHzw==MFzfRtaJ51oSXl71")
            .build();
        HttpResponse<String> response = HTTP_CLIENT
            .send(request, BodyHandlers.ofString());
        final int statusCode = response.statusCode();
        if (statusCode == 502) {
            throw new MalformedURLException("invalid url");
        }
        if (statusCode != 200) {
            throw new IOException("response status code not 200: " + statusCode);
        }
        return response.body().trim();
    } //call

} //WebScraperApi
