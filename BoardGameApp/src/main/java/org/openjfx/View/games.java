package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;

public class games {
    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("homePage");
    }
}
