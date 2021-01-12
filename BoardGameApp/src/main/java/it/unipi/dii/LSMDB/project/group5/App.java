package it.unipi.dii.LSMDB.project.group5;

import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.MongoDBManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

        if(MongoDBManager.createConnection() == true){
            scene = new Scene(loadFXML("LoginPageView"));
            stage.setScene(scene);
            stage.getIcons().add(new Image("file:src/main/resources/img/favicon.png"));
            stage.setTitle("BoardgameApp");
            stage.setHeight(800);
            stage.setWidth(1200);
            stage.show();
        } else {
           logger.log(Level.SEVERE, "Mongo db not starterd");
        }
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

    @Override
    public void stop() {
        // MongoDriver md = MongoDriver.getInstance();
        // Neo4jDriver nd = Neo4jDriver.getInstance();
        // md.close();
        // nd.close();
        Platform.exit();
    }

}