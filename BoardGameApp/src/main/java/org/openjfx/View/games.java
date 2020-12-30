package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;
import java.util.logging.Logger;

public class games {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("homePage");
    }

    @FXML
    void goToGames() throws IOException {
        App.setRoot("games");
    }

    @FXML
    void goToGroups() throws IOException {
        App.setRoot("groups");
    }

    @FXML
    void goToFriends() throws IOException {
        App.setRoot("users");
    }

    @FXML
    void goToSettings() throws IOException {
        App.setRoot("profileSettings");
    }

    @FXML
    void goToGame() throws IOException {
        App.setRoot("game");
    }

    @FXML
    void setSuggestedGames() throws IOException {
        logger.info("Carico i giochi suggeriti");
    }
}
