package it.unipi.dii.lsmdb.project.group5;

import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.MongoDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.Neo4jDBManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Logger.log( "App started");

        if(MongoDBManager.createConnection() && Neo4jDBManager.InitializeDriver()){
            scene = new Scene(loadFXML("LoginPageView"));
            scene.getStylesheets().add("file:src/main/resources/img/style.css");
            stage.setScene(scene);
            stage.getIcons().add(new Image("file:src/main/resources/img/favicon.png"));
            stage.setTitle("BoardgameApp");
            stage.setHeight(700);
            stage.setWidth(1200);
            stage.show();
            stage.centerOnScreen();
        } else if (!MongoDBManager.createConnection()){
           Logger.error("Mongo db not starterd");
        } else {
            Logger.error("Neo4j db not starterd");
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
    public void stop() throws Exception {
        MongoDBManager.close();
        Neo4jDBManager.close();
        Platform.exit();
    }

}