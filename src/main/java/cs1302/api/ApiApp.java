package cs1302.api;

import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import java.net.*;
import java.io.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import cs1302.api.WebScraperApi;

/**
 * An app that takes a user input of a website and returns a word cloud of the text on that website.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox root;
    Text titleText;
    Text descText;
    Text disclaimer;
    HBox urlBar;
    TextField urlField;
    Button loadButton;
    ImageView wordCloud;


    Runnable loadRunnable = () -> {
        try {
            loadButton.setDisable(true);
            URL url = new URL(urlField.getText());
            //testing if its a valid url
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            int responseCode = conn.getResponseCode();
            System.out.println(responseCode == HttpURLConnection.HTTP_OK);

            Image wordCloudImg = WordCloudApi.call(WebScraperApi.scrape(urlField.getText()));
            Platform.runLater(() -> {
                this.wordCloud.setImage(wordCloudImg);
            });
        } catch (IOException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeight(300);
                alert.setWidth(500);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("\"" + urlField.getText() + "\" is not a valid URL" );
                alert.show();
            });
        }


        loadButton.setDisable(false);
    }; //loadRunnable

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox(3);
        titleText = new Text("Word Cloud Generator");
        descText = new Text(
            "Enter a link, and generate a word cloud of the first 200 words on the site!");
        disclaimer = new Text(
            "Note: Due to some websites having pop-ups upon first loading" +
            ", sometimes you only see words like \"cookies\"");
        urlBar = new HBox(5);
        urlField = new TextField("https://");
        loadButton = new Button("Load");
        wordCloud = new ImageView(new Image("file:resources/wordcloud.png"));

        root.setAlignment(Pos.CENTER_LEFT);

        titleText.setTextAlignment(TextAlignment.CENTER);
        descText.setTextAlignment(TextAlignment.CENTER);
        disclaimer.setTextAlignment(TextAlignment.CENTER);

    } // ApiApp


    /** {@inheritDoc} */
    @Override
    public void init() {


        this.loadButton.setOnAction(event -> {
            Thread thread = new Thread(loadRunnable);
            thread.setDaemon(true);
            thread.start();
        });
        this.urlBar.getChildren().addAll(urlField, loadButton);
        this.root.getChildren().addAll(titleText, descText, disclaimer, urlBar, wordCloud);
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        scene = new Scene(root, 710, 800);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

} // ApiApp
