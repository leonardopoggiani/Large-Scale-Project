package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;

public class homePage {

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
        App.setRoot("homePage");
    }

    @FXML
    void goToFriends() throws IOException {
        App.setRoot("homePage");
    }
}
