package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;
import java.util.logging.*;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    Logger logger =  Logger.getLogger(this.getClass().getName());

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Applicazione partita");

        scene = new Scene(loadFXML("login"));
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:src/main/resources/img/favicon.png"));

        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Scene getScene(){
        return scene;
    }

}