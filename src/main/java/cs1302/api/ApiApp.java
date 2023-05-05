package cs1302.api;

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
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox root;
    Text titleText;
    Text descText;
    HBox urlBar;
    TextField urlField;
    Button loadButton;
    ImageView wordCloud;


    Runnable loadRunnable = () -> {
        WebScraperApi.scrape("https://en.wikipedia.org/wiki/Taylor_Swift");
    }; //loadRunnable

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox(3);
        titleText = new Text("Word Cloud Generator");
        descText = new Text(
            "Enter a link, and generate a word cloud of the visible text on the site!");
        urlBar = new HBox(5);
        urlField = new TextField("https://");
        loadButton = new Button("Load");
        wordCloud = new ImageView(new Image("file:resources/wordcloud.png"));

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
        this.root.getChildren().addAll(titleText, descText, urlBar, wordCloud);
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

} // ApiApp
