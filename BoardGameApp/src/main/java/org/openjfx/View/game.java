package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;
import java.util.logging.Logger;

public class game {
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
    void setGameFields() {
    }

    @FXML
    void postReview() {

    }

    @FXML
    void goToGame() {

    }

}
