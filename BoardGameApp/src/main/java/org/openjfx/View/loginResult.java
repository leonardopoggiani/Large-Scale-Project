package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;

public class loginResult {

    @FXML
    void goToHomepage() throws IOException {
        App.setRoot("homePage");
    }
}
